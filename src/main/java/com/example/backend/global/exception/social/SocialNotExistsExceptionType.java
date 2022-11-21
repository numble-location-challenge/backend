package com.example.backend.global.exception.social;

import com.example.backend.global.exception.base.CustomExceptionType;

public enum SocialNotExistsExceptionType implements CustomExceptionType {
    NOT_FOUND_SOCIAL_LIST(-300, "작성된 게시글이 없습니다."),
    NOT_FOUND_SOCIAL(-301, "존재하지 않는 'social' 입니다.");

    private int errorCode;
    private String errorMessage;

    SocialNotExistsExceptionType(int errorCode, String errorMessage) {
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

