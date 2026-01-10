package com.mike.quadtrivia.models;

import com.mike.quadtrivia.enums.ResponseCode;

public record GetQuestionResponse(
        ResponseCode response_code,
        OpenQuestion[] results
) {}
