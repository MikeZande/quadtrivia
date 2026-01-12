package com.mike.quadtrivia.services;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;
import com.mike.quadtrivia.enums.ResponseCode;
import com.mike.quadtrivia.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
public class QuestionServiceTests {
    @Autowired
    private QuestionService questionService;

    @MockitoBean
    private OpenTriviaService openTriviaService;

    @Test
    void TestGetQuestions() {
        // We mock the OpenTriviaResponse.
        OpenQuestion question = new OpenQuestion(
                QuestionType.BOOLEAN,
                Difficulty.EASY,
                "VideoGames",
                "Mario is rood",
                "True",
                List.of("False")
        );
        OpenQuestionResponse mockResponse = new OpenQuestionResponse(ResponseCode.SUCCESS, List.of(question));
        when(openTriviaService.getQuestions(1, null, Difficulty.EASY, QuestionType.BOOLEAN, 1))
                .thenReturn(mockResponse);

        QuestionResponse result = questionService.getQuestions(1, null, Difficulty.EASY, QuestionType.BOOLEAN);
        Question resultingQuestion = result.questions().get(0);

        assertEquals(ResponseCode.SUCCESS, result.responseCode());
        assertEquals(resultingQuestion.question(), question.question());
        assertEquals(resultingQuestion.difficulty(), question.difficulty());
        assertEquals(resultingQuestion.type(), question.type());
        assert(resultingQuestion.answers().contains(question.correct_answer()));
        assert(resultingQuestion.answers().contains(question.incorrect_answers().get(0)));
    }

    @Test
    void TestCheckAnswers() {
        OpenQuestion question = new OpenQuestion(
                QuestionType.BOOLEAN,
                Difficulty.EASY,
                "VideoGames",
                "Mario is rood",
                "True",
                List.of("False")
        );
        OpenQuestionResponse mockResponse = new OpenQuestionResponse(ResponseCode.SUCCESS, List.of(question));
        when(openTriviaService.getQuestions(1, null, Difficulty.EASY, QuestionType.MULTIPLE, 1))
                .thenReturn(mockResponse);

        // Stores the correct answer inside questionService
        String questionId = questionService
                .getQuestions(1, null, Difficulty.EASY, QuestionType.MULTIPLE)
                .questions().get(0)
                .id();

        QuestionAnswerResult correctResult = questionService
                .checkAnswers(List.of(new QuestionAnswer(questionId, "True"))).get(0);
        QuestionAnswerResult wrongResult = questionService
                .checkAnswers(List.of(new QuestionAnswer(questionId, "False"))).get(0);

        assertEquals(correctResult, new QuestionAnswerResult(questionId, true));
        assertEquals(wrongResult, new QuestionAnswerResult(questionId, false));
    }
}
