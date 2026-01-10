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
    private final RestTemplate restTemplate;
    private String token;

    public OpenTriviaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        updateSessionToken();
    }

    private void updateSessionToken() {
        String uri = API_URI + "api_token.php?command=request";

        try {
            TokenResponse tokenResponse = restTemplate.getForObject(uri, TokenResponse.class);
            if (tokenResponse != null) {
                token = tokenResponse.token();
            }
        } catch (Exception e) {
            System.out.println("Unexpected error occurred. " + e.getMessage());
        }
    }

    OpenQuestionResponse getQuestions(int amount, Integer category, Difficulty difficulty, QuestionType type) {
        String uri = API_URI + "api.php?amount=" + amount;

        if (category != null) {
            uri += "&category=" + category;
        }
        if (difficulty != null) {
            uri += "&difficulty=" + difficulty.getDifficulty();
        }
        if (type != null) {
            uri += "&type=" + type.getType();
        }

        // We only use a token when certain that the dataset is big enough to fetch 50 questions.
        if (category == null && difficulty == null && type == null) {
            uri += "&token=" + token;
        }

        try {
            ResponseEntity<OpenQuestionResponse> entity = restTemplate.getForEntity(uri, OpenQuestionResponse.class);
            OpenQuestionResponse response = entity.getBody();

            // Refresh token when needed.
            if (response != null &&
                (response.response_code() == ResponseCode.TOKEN_EMPTY ||
                response.response_code() == ResponseCode.TOKEN_NOT_FOUND))
            {
                updateSessionToken();
                Thread.sleep(5100); // Prevent rate limit.
                return getQuestions(amount, category, difficulty, type);
            }
            return response;
        } catch (HttpClientErrorException e) {
            return e.getResponseBodyAs(OpenQuestionResponse.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}