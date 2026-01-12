package com.mike.quadtrivia.controllers;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;
import com.mike.quadtrivia.enums.ResponseCode;
import com.mike.quadtrivia.models.QuestionAnswer;
import com.mike.quadtrivia.models.QuestionAnswerResult;
import com.mike.quadtrivia.models.QuestionResponse;
import com.mike.quadtrivia.services.QuestionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/questions")
    public ResponseEntity<QuestionResponse> getQuestions(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(defaultValue = "5") Integer amount,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String type
    ) {
        String userId = getUserId(request, response);

        Difficulty difficultyEnum = Difficulty.fromValue(difficulty);
        QuestionType questionTypeEnum = QuestionType.fromValue(type);

        QuestionResponse body = questionService.getQuestions(amount, category, difficultyEnum, questionTypeEnum, userId);

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
    public ResponseEntity<List<QuestionAnswerResult>> checkAnswers(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody List<QuestionAnswer> answers
    ) {

        String userId = getUserId(request, response);

        List<QuestionAnswerResult> body = questionService.checkAnswers(answers, userId);

        return ResponseEntity.ok(body);
    }

    private String getUserId(HttpServletRequest request, HttpServletResponse response) {
        // Check if cookie already exists
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("userId".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Generate new cookie if it does not exist
        String uuid = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("userId", uuid);
        cookie.setPath("/");
        cookie.setMaxAge(Integer.MAX_VALUE); // 1 day
        response.addCookie(cookie);

        return uuid;
    }
}
