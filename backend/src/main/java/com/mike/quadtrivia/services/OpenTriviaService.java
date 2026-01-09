package com.mike.quadtrivia.services;

import com.mike.quadtrivia.models.TokenResponse;
import org.springframework.stereotype.Service;
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
        TokenResponse tokenResponse = restTemplate.getForObject(uri, TokenResponse.class);

        if (tokenResponse != null) { // TODO: Add some better verification, regarding response codes.
            token = tokenResponse.token();
        }
    }

    public String getQuestions(int amount, String category, String difficulty, String type) {
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

        String result = restTemplate.getForObject(uri, String.class);
        //TODO: Verify result

        return result;
    }

    public void getQuestions() { getQuestions(10, null, null, null);}
}