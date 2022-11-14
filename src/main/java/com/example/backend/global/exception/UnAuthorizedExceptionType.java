package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomExceptionType;

/**
 * HttpStatus: UNAUTHORIZED
 */
public enum UnAuthorizedExceptionType implements CustomExceptionType {
    USER_UN_AUTHORIZED(-188, "Access Token이 없거나 유효하지 않습니다. '/refresh' POST로 재요청 하십시오.");

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
