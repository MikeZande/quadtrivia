package com.mike.quadtrivia.services;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;
import com.mike.quadtrivia.enums.ResponseCode;
import com.mike.quadtrivia.models.OpenQuestionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
    Tests that the OpenTriviaService behaves as expected.
    We sleep 5.1 seconds to prevent the timeout error.
 */
@SpringBootTest
public class OpenTriviaServiceTests {
    @Autowired
    private OpenTriviaService triviaService;

    @Test
    void basicTest() {
        sleep();
        OpenQuestionResponse result = triviaService.getQuestions(1, 27, Difficulty.EASY, QuestionType.BOOLEAN, 1);
        assert(result.response_code() == ResponseCode.SUCCESS);
    }

    @Test
    void testRateLimit() {
        sleep();
        OpenQuestionResponse result = triviaService.getQuestions(5, null, null, null, 1);
        assert(result.response_code() == ResponseCode.SUCCESS);

        result = triviaService.getQuestions(5, null, null, null, 1);
        assert(result.response_code() == ResponseCode.RATE_LIMIT_EXCEEDED);
    }

    @Test
    void testTokenRefresh() {
        // Exhaust the token. (by checking using an external tool it is known that category 30 has 32 questions)
        sleep();
        OpenQuestionResponse result = triviaService.getQuestions(32, 30, null, null, 1);
        assert(result != null);
        assert(result.response_code() == ResponseCode.SUCCESS);

        // Now we check that the token gets refreshed by our service.
        sleep();
        result = triviaService.getQuestions(1, 30, null, null, 1);
        assert(result != null);
        assert(result.response_code() == ResponseCode.SUCCESS);
    }

    private void sleep() {
        try {
            Thread.sleep(5100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}