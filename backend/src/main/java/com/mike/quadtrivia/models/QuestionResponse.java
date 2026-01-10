package com.mike.quadtrivia.models;

import com.mike.quadtrivia.enums.ResponseCode;

import java.util.List;

public record QuestionResponse(
        ResponseCode responseCode,
        List<Question> questions
) {}