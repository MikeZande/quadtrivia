package com.mike.quadtrivia.services;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;
import com.mike.quadtrivia.enums.ResponseCode;
import com.mike.quadtrivia.models.OpenQuestionResponse;
import com.mike.quadtrivia.models.OpenQuestion;
import com.mike.quadtrivia.models.Question;
import com.mike.quadtrivia.models.QuestionResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
    Internal access point for extracting questions and getting answers.
 */
@Service
public class QuestionService {
    private final Map<String, String> questionAnswerMap = new ConcurrentHashMap<>();
    private final OpenTriviaService openTriviaService;

    public QuestionService(OpenTriviaService openTriviaService) {
        this.openTriviaService = openTriviaService;
    }

    public QuestionResponse getQuestions(int amount, Integer category, Difficulty difficulty, QuestionType type) {
        QuestionResponse questionResponse;
        ResponseCode responseCode;
        List<Question> questions;
        List<OpenQuestion> openQuestions;

        OpenQuestionResponse response = openTriviaService.getQuestions(amount, category, difficulty, type);

        if (response == null) {
            return null;
        }

        responseCode = response.response_code();
        openQuestions = response.results();

        questions = convertQuestions(openQuestions);
        questionResponse = new QuestionResponse(responseCode, questions);

        return questionResponse;
    }

    public void checkAnswers() {

    }

    private List<Question> convertQuestions(List<OpenQuestion> questions) {
        List<Question> questionList = new ArrayList<>();

        if (questions == null) {
            return questionList;
        }

        for (OpenQuestion openQuestion : questions) {
            List<String> answers = new ArrayList<>();

            for (String answer : openQuestion.incorrect_answers()) {
                // Converts html formatting into human-readable string.
                answers.add(HtmlUtils.htmlUnescape(answer));
            }
            answers.add(HtmlUtils.htmlUnescape(openQuestion.correct_answer()));
            Collections.shuffle(answers); // Shuffle the answers so that the order doesn't give away the right answer.

            // Store correct answer in the hashmap.
            // TODO: Slightly concerned about duplicates ending up in the hashmap because of token refresh.
            String questionId = UUID.randomUUID().toString();
            questionAnswerMap.put(questionId, openQuestion.correct_answer());

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