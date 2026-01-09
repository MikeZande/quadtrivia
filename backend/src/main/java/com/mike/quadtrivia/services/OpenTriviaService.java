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
}