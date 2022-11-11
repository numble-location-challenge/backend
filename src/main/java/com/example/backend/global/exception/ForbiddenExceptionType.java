package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomExceptionType;

/**
 * HttpStatus: FORBIDDEN
 */
public enum ForbiddenExceptionType implements CustomExceptionType {
    USER_UN_AUTHORIZED(-199, "권한이 없는 페이지입니다.");

    private int errorCode;
    private String errorMessage;

    ForbiddenExceptionType(int errorCode, String errorMessage) {
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
