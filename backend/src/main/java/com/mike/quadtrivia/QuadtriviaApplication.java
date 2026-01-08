package com.mike.quadtrivia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class QuadtriviaApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuadtriviaApplication.class, args);
	}

	@GetMapping("/")
	public String home() {
		return "Hello this is Quad Trivia";
	}
}
