package com.mike.quadtrivia.models;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;

/*
    Defines the questions returned by this API.
 */
public record Question(
        QuestionType type,
        Difficulty difficulty,
        String category,
        String question
) {}