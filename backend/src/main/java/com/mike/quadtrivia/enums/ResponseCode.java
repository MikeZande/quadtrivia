package com.mike.quadtrivia.enums;

import tools.jackson.databind.annotation.JsonDeserialize;

public enum ResponseCode {
    SUCCESS(0),
    NO_RESULTS(1),
    INVALID_PARAMETER(2),
    TOKEN_NOT_FOUND(3),
    TOKEN_EMPTY(4),
    RATE_LIMIT_EXCEEDED(5);

    ResponseCode(int code) {}
}
