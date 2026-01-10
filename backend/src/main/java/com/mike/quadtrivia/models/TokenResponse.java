package com.mike.quadtrivia.models;

import com.mike.quadtrivia.enums.ResponseCode;

public record TokenResponse(
        ResponseCode response_code,
        String response_message,
        String token
) {}