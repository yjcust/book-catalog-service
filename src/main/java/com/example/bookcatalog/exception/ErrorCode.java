package com.example.bookcatalog.exception;

public enum ErrorCode {
    SUCCESS(0, "成功"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_FOUND(40400, "数据未找到"),
    SYSTEM_ERROR(50000, "系统内部异常");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
