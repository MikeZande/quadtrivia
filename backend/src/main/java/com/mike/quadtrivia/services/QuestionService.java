package com.mike.quadtrivia.services;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;
import com.mike.quadtrivia.models.*;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
    Internal access point for extracting questions and getting answers.
 */
@Service
public class QuestionService {
    // Stores the correct answers of the most recently requested questions for each user.
    private final Map<String, List<QuestionAnswer>> userToCorrectAnswersMap = new ConcurrentHashMap<>();
    private final OpenTriviaService openTriviaService;

    public QuestionService(OpenTriviaService openTriviaService) {
        this.openTriviaService = openTriviaService;
    }

    public QuestionResponse getQuestions(int amount, Integer category, Difficulty difficulty, QuestionType type, String userId) {

        OpenQuestionResponse response = openTriviaService.getQuestions(amount, category, difficulty, type, 1);

        List<Question> questions = convertQuestions(response.results(), userId);

        return new QuestionResponse(response.response_code(), questions);
    }

    public List<QuestionAnswerResult> checkAnswers(List<QuestionAnswer> submittedAnswers, String userId) {
        List<QuestionAnswerResult> results = new ArrayList<>();
        List<QuestionAnswer> correctAnswers = getCorrectAnswers(userId);

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
    *   Also stores the correct answers and id, into correctAnswers.
    */
    private List<Question> convertQuestions(List<OpenQuestion> questions, String userId) {
        List<Question> questionList = new ArrayList<>();
        List<QuestionAnswer> correctAnswers = getCorrectAnswers(userId);
        correctAnswers.clear(); // User only interacts with the last received questions.

        if (questions == null) {
            return questionList;
        }

        for (OpenQuestion openQuestion : questions) {
            List<String> answers = new ArrayList<>();
            // Converts html formatting from OpenTriviaDB into human-readable string.
            String correctAnswer = HtmlUtils.htmlUnescape(openQuestion.correct_answer());

            for (String answer : openQuestion.incorrect_answers()) {
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

    private List<QuestionAnswer> getCorrectAnswers(String userId) {
        return userToCorrectAnswersMap.computeIfAbsent(userId, key -> new ArrayList<>());
    }
}