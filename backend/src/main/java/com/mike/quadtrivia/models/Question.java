package com.mike.quadtrivia.models;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;

import java.util.List;

/*
    Defines the questions returned by this API.
 */
public record Question(
        String id,
        QuestionType type,
        Difficulty difficulty,
        String category,
        String question,
        List<String> answers
) {}