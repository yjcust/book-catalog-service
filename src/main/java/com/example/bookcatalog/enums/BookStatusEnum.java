package com.example.bookcatalog.enums;

import lombok.Getter;

@Getter
public enum BookStatusEnum {
    NORMAL(0, "正常"),
    UNDER_REVIEW(1, "审核中");

    private final int code;
    private final String message;

    BookStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
