package com.mike.quadtrivia.controllers;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;
import com.mike.quadtrivia.models.QuestionResponse;
import com.mike.quadtrivia.services.QuestionService;
import org.springframework.web.bind.annotation.*;

@RestController
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/questions")
    public QuestionResponse getQuestions(int amount, Integer category, Difficulty difficulty, QuestionType type) {
        return questionService.getQuestions(amount, category, difficulty, type);
    }

//    @PostMapping("/checkanswers")
//    public Map<String, Boolean> checkAnswers(@RequestBody Map<String, String> answers) {
//        return questionService.checkAnswers(answers);
//    }
}
