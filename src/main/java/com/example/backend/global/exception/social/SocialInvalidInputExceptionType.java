package com.example.backend.global.exception.social;

import com.example.backend.global.exception.base.CustomExceptionType;

/**
 * HttpStatus: BAD_REQUEST
 */
public enum SocialInvalidInputExceptionType implements CustomExceptionType {
    OUT_OF_RANGE_OF_INPUT(-310,"입력 범위를 벗어 났습니다. 1~3 사이 값만 입력해주세요.");

    private int errorCode;
    private String errorMessage;

    SocialInvalidInputExceptionType(int errorCode, String errorMessage) {
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
