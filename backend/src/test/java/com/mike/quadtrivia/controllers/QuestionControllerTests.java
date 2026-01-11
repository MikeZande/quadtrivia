package com.mike.quadtrivia.controllers;

import com.mike.quadtrivia.enums.ResponseCode;
import com.mike.quadtrivia.models.QuestionResponse;
import com.mike.quadtrivia.services.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class QuestionControllerTests {

    @Autowired
    private QuestionController questionController;

    @MockitoBean
    private QuestionService questionService;

    @Test
    void testGetQuestions() {
        QuestionResponse mockResponse = new QuestionResponse(ResponseCode.SUCCESS, List.of());
        when(questionService.getQuestions(5, null, null, null)).thenReturn(mockResponse);

        ResponseEntity<QuestionResponse> response = questionController.getQuestions(5, null, null, null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(ResponseCode.SUCCESS, response.getBody().responseCode());
    }

    @Test
    void testGetQuestionsRateLimit() {
        QuestionResponse mockResponse = new QuestionResponse(ResponseCode.RATE_LIMIT_EXCEEDED, List.of());
        when(questionService.getQuestions(5, null, null, null)).thenReturn(mockResponse);

        ResponseEntity<QuestionResponse> response = questionController.getQuestions(5, null, null, null);

        assertEquals(429, response.getStatusCode().value());
        assertEquals(ResponseCode.RATE_LIMIT_EXCEEDED, response.getBody().responseCode());
    }
}
