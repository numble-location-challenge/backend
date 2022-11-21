package com.example.backend.global.exception.social;

import com.example.backend.global.exception.base.CustomException;
import com.example.backend.global.exception.base.CustomExceptionType;

public class SocialNotExistsException extends CustomException {

    private SocialNotExistsExceptionType exceptionType;

    public SocialNotExistsException(SocialNotExistsExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public CustomExceptionType getExceptionType() {
        return exceptionType;
    }
}
