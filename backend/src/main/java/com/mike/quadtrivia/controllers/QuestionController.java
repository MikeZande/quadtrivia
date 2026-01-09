package com.mike.quadtrivia.controllers;

import com.mike.quadtrivia.services.OpenTriviaService;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuestionController {
    private final OpenTriviaService triviaService;

    public QuestionController(OpenTriviaService triviaService) {
        this.triviaService = triviaService;
    }
}
