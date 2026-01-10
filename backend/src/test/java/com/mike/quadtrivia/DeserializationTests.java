package com.mike.quadtrivia;

import com.mike.quadtrivia.enums.Difficulty;
import com.mike.quadtrivia.enums.QuestionType;
import com.mike.quadtrivia.models.OpenQuestion;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

public class DeserializationTests {
    @Test
    void testDifficultyDeserialization() {
        String json = "\"easy\"";
        Difficulty difficulty = new ObjectMapper().readValue(json, Difficulty.class);
        assert(difficulty == Difficulty.EASY);
    }

    @Test
    void testQuestionTypeDeserialization() {
        String json = "\"multiple\"";
        QuestionType type = new ObjectMapper().readValue(json, QuestionType.class);
        assert(type == QuestionType.MULTIPLE);
    }

    @Test
    void testQuestionDeserialization() {
        String json =
                "{\n" +
                        "\"type\": \"multiple\",\n" +
                        "\"difficulty\": \"easy\",\n" +
                        "\"category\": \"Entertainment: Video Games\",\n" +
                        "\"question\": \"In the original Spyro game who is the first villain?\",\n" +
                        "\"correct_answer\": \"Gnasty Gnorc\",\n" +
                        "\"incorrect_answers\": [\"Ripto\", \"Sorceress\", \"Cynder\"]\n" +
                "}";

        OpenQuestion result = new ObjectMapper().readValue(json, OpenQuestion.class);

        OpenQuestion expectedResult = new OpenQuestion(
                QuestionType.MULTIPLE,
                Difficulty.EASY,
                "Entertainment: Video Games",
                "In the original Spyro game who is the first villain?",
                "Gnasty Gnorc",
                List.of("Ripto", "Sorceress", "Cynder"));

        assert(result.equals(expectedResult));
    }
}
