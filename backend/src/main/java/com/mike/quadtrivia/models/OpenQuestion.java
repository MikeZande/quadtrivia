package com.mike.quadtrivia.models;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;

import java.util.List;

/*
    This record is responsible for defining the questions received from the OpenTrivia DB API.
 */
public record OpenQuestion(
        QuestionType type,
        Difficulty difficulty,
        String category,
        String question,
        String correct_answer,
        List<String> incorrect_answers
) {}