package com.mike.quadtrivia.controllers;

import com.mike.quadtrivia.enums.ResponseCode;
import com.mike.quadtrivia.models.QuestionResponse;
import com.mike.quadtrivia.services.QuestionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class QuestionControllerTests {
    private final String userId = "fake_cookie";
    private final Cookie[] cookie = {new Cookie("userId", userId)};

    @Autowired
    private QuestionController questionController;

    @MockitoBean
    private QuestionService questionService;

    @Test
    void testGetQuestions() {
        QuestionResponse mockResponse = new QuestionResponse(ResponseCode.SUCCESS, List.of());
        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockHttpResponse = mock(HttpServletResponse.class);

        when(mockHttpRequest.getCookies()).thenReturn(cookie);
        when(questionService.getQuestions(5, null, null, null, userId)).thenReturn(mockResponse);

        ResponseEntity<QuestionResponse> response = questionController.getQuestions(mockHttpRequest, mockHttpResponse,5, null, null, null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(ResponseCode.SUCCESS, response.getBody().responseCode());
    }

    @Test
    void testGetQuestionsRateLimit() {
        QuestionResponse mockResponse = new QuestionResponse(ResponseCode.RATE_LIMIT_EXCEEDED, List.of());
        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockHttpResponse = mock(HttpServletResponse.class);

        when(mockHttpRequest.getCookies()).thenReturn(cookie);
        when(questionService.getQuestions(5, null, null, null, userId)).thenReturn(mockResponse);

        ResponseEntity<QuestionResponse> response = questionController.getQuestions(mockHttpRequest, mockHttpResponse, 5, null, null, null);

        assertEquals(429, response.getStatusCode().value());
        assertEquals(ResponseCode.RATE_LIMIT_EXCEEDED, response.getBody().responseCode());
    }
}
