package com.mike.quadtrivia.services;

import com.mike.quadtrivia.enums.ResponseCode;
import com.mike.quadtrivia.models.GetQuestionResponse;
import com.mike.quadtrivia.models.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/*
    Responsible for accessing the Open Trivia DB API.
    As well as storing state regarding the session token.
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

    public GetQuestionResponse getQuestions(int amount, Integer category, String difficulty, String type) {
        String uri = API_URI + "api.php?token=" + token;

        uri += "&amount=" + amount;
        if (category != null) {
            uri += "&category=" + category;
        }
        if (difficulty != null) {
            uri += "&difficulty=" + difficulty;
        }
        if (type != null) {
            uri += "&type=" + type;
        }

        try {
            ResponseEntity<GetQuestionResponse> entity = restTemplate.getForEntity(uri, GetQuestionResponse.class);
            GetQuestionResponse response = entity.getBody();

            // Refresh token when needed.
            if (response != null &&
                (response.response_code() == ResponseCode.TOKEN_EMPTY ||
                response.response_code() == ResponseCode.TOKEN_NOT_FOUND))
            {
                updateSessionToken();
                Thread.sleep(5100);
                return getQuestions(amount, category, difficulty, type);
            }
            return response;
        } catch (HttpClientErrorException e) {
            // This should only happen when a rate limit happens.
            return e.getResponseBodyAs(GetQuestionResponse.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public GetQuestionResponse getQuestions() {
        return getQuestions(10, null, null, null);
    }
}