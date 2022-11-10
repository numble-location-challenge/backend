package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomExceptionType;

/**
 * HttpStatus: UNAUTHORIZED
 */
public enum UnAuthorizedExceptionType implements CustomExceptionType {
    USER_UN_AUTHORIZED(-199, "권한이 없는 페이지입니다.");

    private int errorCode;
    private String errorMessage;

    UnAuthorizedExceptionType(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public int getErrorCode() {
        return 0;
    }
}
