package com.mike.quadtrivia;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;
import com.mike.quadtrivia.models.Question;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public class SerializationTests {
    @Test
    void testDifficultySerialization() {
        Difficulty difficulty = Difficulty.EASY;
        String json = new ObjectMapper().writeValueAsString(difficulty);
        String expected = "\"easy\"";
        assert(json.equals(expected));
    }

    @Test
    void testQuestionTypeSerialization() {
        QuestionType type = QuestionType.BOOLEAN;
        String json = new ObjectMapper().writeValueAsString(type);
        String expected = "\"boolean\"";
        assert(json.equals(expected));
    }

    @Test
    void testQuestionSerialization() {
        Question question = new Question(
                "1",
                QuestionType.MULTIPLE,
                Difficulty.EASY,
                "Entertainment: Video Games",
                "In the original Spyro game who is the first villain?",
                List.of("Gnasty Gnorc", "Ripto", "Sorceress", "Cynder")
                );

        String json = new ObjectMapper().writeValueAsString(question);

        String expectedResult =
                "{" +
                        "\"type\":\"multiple\"," +
                        "\"difficulty\":\"easy\"," +
                        "\"category\":\"Entertainment: Video Games\"," +
                        "\"question\":\"In the original Spyro game who is the first villain?\"," +
                        "\"answers\":[\"Gnasty Gnorc\",\"Ripto\",\"Sorceress\",\"Cynder\"]" +
                "}";

        System.out.println(json);
        System.out.println(expectedResult);

        assert(json.equals(expectedResult));
    }
}
