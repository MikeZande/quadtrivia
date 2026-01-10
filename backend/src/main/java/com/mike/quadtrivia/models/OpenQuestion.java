package com.mike.quadtrivia.models;

/*
    This record is responsible for defining the questions received from the OpenTrivia DB API.
 */
public record OpenQuestion(
        String type,
        String difficulty,
        String category,
        String question,
        String correct_answer,
        String[] incorrect_answers
) {}