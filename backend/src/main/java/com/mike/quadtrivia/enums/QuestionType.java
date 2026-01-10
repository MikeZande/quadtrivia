package com.mike.quadtrivia.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum QuestionType {
    MULTIPLE("multiple"),
    BOOLEAN("boolean"),;

    private final String type;

    QuestionType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public QuestionType fromValue(String value) {
        for (QuestionType qt : values()) {
            if (qt.type.equals(value)) {
                return qt;
            }
        }
        return null;
    }
}
