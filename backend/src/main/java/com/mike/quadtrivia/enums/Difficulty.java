package com.mike.quadtrivia.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Difficulty {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    private final String difficulty;

    Difficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @JsonValue
    public String getDifficulty() {
        return difficulty;
    }

    @JsonCreator
    public static Difficulty fromValue(String difficulty) {
        for (Difficulty d : Difficulty.values()) {
            if (d.getDifficulty().equals(difficulty)) {
                return d;
            }
        }
        return null;
    }
}
