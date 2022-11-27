package com.example.backend.global.exception.feed;

import com.example.backend.global.exception.base.CustomExceptionType;

public enum FeedInvalidInputExceptionType implements CustomExceptionType {
    INVALID_INPUT_FILTER(-204, "잘못된 필터링 조건입니다.");

    private int errorCode;
    private String errorMessage;

    FeedInvalidInputExceptionType(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }
}
