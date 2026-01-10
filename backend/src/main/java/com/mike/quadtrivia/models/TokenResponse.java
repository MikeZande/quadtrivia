package com.mike.quadtrivia.models;

import com.mike.quadtrivia.enums.ResponseCode;
import tools.jackson.databind.annotation.JsonDeserialize;

public record TokenResponse(
        ResponseCode response_code,
        String response_message,
        String token
) {}