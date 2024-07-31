package com.pbhuy.identityservice.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception"),
    INVALID_MESSAGE_KEY(1001, "Invalid message key"),
    USER_ALREADY_EXISTED(1002, "User already existed"),
    INVALID_USERNAME(1003, "Username must be at least 3 characters"),
    INVALID_PASSWORD(1004, "Password must be at least 6 characters"),
    USER_NOT_FOUND(1005, "User not found");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
