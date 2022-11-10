package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomExceptionType;

/**
 * HttpStatus: BAD_REQUEST
 */
public enum InvalidInputExceptionType implements CustomExceptionType {
    ALREADY_EXISTS_EMAIL(-101,"'email'(body)이 이미 존재합니다."),
    ALREADY_EXISTS_NICKNAME(-102,"'nickname'(body)이 이미 존재합니다."),
    ALREADY_EXIST_EMAIL_AND_NICKNAME( -103,"'email'(body)과 'nickname'(body)이 이미 존재합니다."),
    ACCOUNT_NOT_MATCH(-104,"'email'(body)또는 'password'(body)가 매칭되지 않습니다.");

    private int errorCode;
    private String errorMessage;

    InvalidInputExceptionType(int errorCode, String errorMessage) {
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