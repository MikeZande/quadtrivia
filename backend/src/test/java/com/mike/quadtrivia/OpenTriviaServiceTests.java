package com.mike.quadtrivia;

import com.mike.quadtrivia.enums.ResponseCode;
import com.mike.quadtrivia.models.GetQuestionResponse;
import com.mike.quadtrivia.services.OpenTriviaService;
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
    void testWithoutParameters() {
        sleep();
        GetQuestionResponse result = triviaService.getQuestions();
        assert(result != null);
        assert(result.response_code() == ResponseCode.SUCCESS);
    }

    @Test
    void testWithParameters() {
        sleep();
        GetQuestionResponse result = triviaService.getQuestions(1, 27, "easy", "boolean");
        assert(result != null);
        assert(result.response_code() == ResponseCode.SUCCESS);
    }

    @Test
    void testRateLimit() {
        sleep();
        GetQuestionResponse result = triviaService.getQuestions();
        assert(result != null);
        assert(result.response_code() == ResponseCode.SUCCESS);

        result = triviaService.getQuestions();
        assert(result != null);
        assert(result.response_code() == ResponseCode.RATE_LIMIT_EXCEEDED);
    }

    @Test
    void testInvalidParameter() {
        sleep();
        GetQuestionResponse result = triviaService.getQuestions(5, 27, "easy", "invalid type");
        assert(result != null);
        assert(result.response_code() == ResponseCode.INVALID_PARAMETER);
    }

    /*
        Not ideal as this test depends on that the state of the Open Trivia DB API does not change.

        I would want to check that the value of the token is different at the start compared to the end.
        However I think adding a token getter to the service is unnecessary and bad.
     */
    @Test
    void testTokenRefresh() {
        // Exhaust the token.
        sleep();
        GetQuestionResponse result = triviaService.getQuestions(32, 30, null, null);
        assert(result != null);
        assert(result.response_code() == ResponseCode.SUCCESS);

        // Now we check that the token gets refreshed by our service.
        sleep();
        result = triviaService.getQuestions(1, 30, null, null);
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
