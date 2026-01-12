package com.mike.quadtrivia.services;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;
import com.mike.quadtrivia.enums.ResponseCode;
import com.mike.quadtrivia.models.*;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/*
    Internal access point for extracting questions and getting answers.
 */
@Service
public class QuestionService {
    private final List<QuestionAnswer> correctAnswers = new ArrayList<>();
    private final OpenTriviaService openTriviaService;

    public QuestionService(OpenTriviaService openTriviaService) {
        this.openTriviaService = openTriviaService;
    }

    public QuestionResponse getQuestions(int amount, Integer category, Difficulty difficulty, QuestionType type) {
        QuestionResponse questionResponse;
        ResponseCode responseCode;
        List<Question> questions;
        List<OpenQuestion> openQuestions;

        OpenQuestionResponse response = openTriviaService.getQuestions(amount, category, difficulty, type, 1);

        responseCode = response.response_code();
        openQuestions = response.results();

        questions = convertQuestions(openQuestions);
        questionResponse = new QuestionResponse(responseCode, questions);

        return questionResponse;
    }

    public List<QuestionAnswerResult> checkAnswers(List<QuestionAnswer> submittedAnswers) {
        List<QuestionAnswerResult> results = new ArrayList<>();

        for (QuestionAnswer answer : submittedAnswers) {
            if (correctAnswers.contains(answer)) {
                results.add(new QuestionAnswerResult(answer.questionId(), true));
            } else {
                results.add(new QuestionAnswerResult(answer.questionId(), false));
            }
        }

        return results;
    }

    /*
    *   Converts OpenQuestions into Questions,
    *   Also stores a link between the correct answer and a question id into questionAnswerMap.
    */
    private List<Question> convertQuestions(List<OpenQuestion> questions) {
        List<Question> questionList = new ArrayList<>();
        correctAnswers.clear(); // User only interacts with the lastly received questions.

        if (questions == null) {
            return questionList;
        }

        for (OpenQuestion openQuestion : questions) {
            List<String> answers = new ArrayList<>();
            String correctAnswer = HtmlUtils.htmlUnescape(openQuestion.correct_answer());

            for (String answer : openQuestion.incorrect_answers()) {
                // Converts html formatting into human-readable string.
                answers.add(HtmlUtils.htmlUnescape(answer));
            }
            answers.add(correctAnswer);
            Collections.shuffle(answers); // Shuffle the answers so that the order doesn't give away the right answer.

            // Store correct answer in the hashmap.
            String questionId = UUID.randomUUID().toString();
            correctAnswers.add(new QuestionAnswer(questionId, correctAnswer));

            Question question = new Question(
                questionId,
                openQuestion.type(),
                openQuestion.difficulty(),
                HtmlUtils.htmlUnescape(openQuestion.category()),
                HtmlUtils.htmlUnescape(openQuestion.question()),
                answers
            );

            questionList.add(question);
        }

        return questionList;
    }
}