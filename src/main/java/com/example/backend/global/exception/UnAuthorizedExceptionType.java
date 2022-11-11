package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomExceptionType;

/**
 * HttpStatus: UNAUTHORIZED
 */
public enum UnAuthorizedExceptionType implements CustomExceptionType {
    USER_UN_AUTHORIZED(-188, "인증되지 않은 유저입니다.");

    private int errorCode;
    private String errorMessage;

    UnAuthorizedExceptionType(int errorCode, String errorMessage) {
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
