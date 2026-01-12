package com.mike.quadtrivia.services;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;
import com.mike.quadtrivia.enums.ResponseCode;
import com.mike.quadtrivia.models.OpenQuestionResponse;
import com.mike.quadtrivia.models.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/*
    Responsible for accessing the Open Trivia DB API.
 */
@Service
public class OpenTriviaService {
    private final String API_URI = "https://opentdb.com/";
    private String token;

    public OpenTriviaService() {
        updateSessionToken();
    }

    private void updateSessionToken() {
        String uri = API_URI + "api_token.php?command=request";

        TokenResponse tokenResponse = new RestTemplate().getForObject(uri, TokenResponse.class);

        if (tokenResponse != null) {
            token = tokenResponse.token();
        }
    }

    /*
        Retries makes sure the function does not infinitely loop.
        (The open trivia db can return responseCode.TOKEN_EMPTY even when this is not the case)
     */
    OpenQuestionResponse getQuestions(int amount, Integer category, Difficulty difficulty, QuestionType type, int retries) {
        String uri = API_URI + "api.php?amount=" + amount + "&token=" + token;

        if (category != null) {
            uri += "&category=" + category;
        }
        if (difficulty != null) {
            uri += "&difficulty=" + difficulty.getDifficulty();
        }
        if (type != null) {
            uri += "&type=" + type.getType();
        }

        try {
            ResponseEntity<OpenQuestionResponse> entity = new RestTemplate().getForEntity(uri, OpenQuestionResponse.class);
            OpenQuestionResponse response = entity.getBody();

            // Refresh token when needed.
            if (response != null && retries > 0 &&
                (response.response_code() == ResponseCode.TOKEN_EMPTY ||
                response.response_code() == ResponseCode.TOKEN_NOT_FOUND))
            {
                updateSessionToken();
                Thread.sleep(5100); // Prevent rate limit.
                return getQuestions(amount, category, difficulty, type, retries - 1);
            }

            return response;
        } catch (HttpClientErrorException e) {
            return e.getResponseBodyAs(OpenQuestionResponse.class);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for API rate limit", e);
        }
    }
}