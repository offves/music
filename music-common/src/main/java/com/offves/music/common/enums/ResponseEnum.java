package com.offves.music.common.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {

    SUCCESS(200, "OK"),
    SERVER_ERROR(500, "server error"),
    PARAM_IS_EMPTY(1000, "param must not be empty"),
    ;

    private final Integer code;

    private final String message;

    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
