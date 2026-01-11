package com.mike.quadtrivia.controllers;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;
import com.mike.quadtrivia.enums.ResponseCode;
import com.mike.quadtrivia.models.QuestionAnswer;
import com.mike.quadtrivia.models.QuestionAnswerResult;
import com.mike.quadtrivia.models.QuestionResponse;
import com.mike.quadtrivia.services.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/questions")
    public ResponseEntity<QuestionResponse> getQuestions(
            @RequestParam(defaultValue = "5") Integer amount,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String type
    ) {

        Difficulty difficultyEnum = Difficulty.fromValue(difficulty);
        QuestionType questionTypeEnum = QuestionType.fromValue(type);

        QuestionResponse body = questionService.getQuestions(amount, category, difficultyEnum, questionTypeEnum);

        if (body.responseCode() == ResponseCode.RATE_LIMIT_EXCEEDED) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(body);
        }

        if (body.responseCode() == ResponseCode.INVALID_PARAMETER) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(body);
        }

        return ResponseEntity.ok(body);
    }

    @PostMapping("/checkanswers")
    public ResponseEntity<List<QuestionAnswerResult>> checkAnswers(@RequestBody List<QuestionAnswer> answers) {

        List<QuestionAnswerResult> body = questionService.checkAnswers(answers);

        return ResponseEntity.ok(body);
    }
}
