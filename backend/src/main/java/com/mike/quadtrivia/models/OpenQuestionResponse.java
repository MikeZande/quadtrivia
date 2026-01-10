package com.mike.quadtrivia.models;

import com.mike.quadtrivia.enums.ResponseCode;

import java.util.List;

public record OpenQuestionResponse(
        ResponseCode response_code,
        List<OpenQuestion> results
) {}