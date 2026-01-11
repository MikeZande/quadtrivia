package com.mike.quadtrivia.models;

public record QuestionAnswerResult(
        String questionId,
        Boolean isCorrectAnswer
) {}
