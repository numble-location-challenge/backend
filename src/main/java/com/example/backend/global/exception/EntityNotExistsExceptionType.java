package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomExceptionType;

/**
 * HttpStatus: NOT_FOUND
 */
public enum EntityNotExistsExceptionType implements CustomExceptionType {
    NOT_FOUND_USER(-111, "존재하지 않는 'user' 입니다."),
    NOT_FOUND_KAKAO_USER(-112, "회원가입 되지 않은 카카오 계정입니다.");

    private int errorCode;
    private String errorMessage;

    EntityNotExistsExceptionType(int errorCode, String errorMessage) {
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
