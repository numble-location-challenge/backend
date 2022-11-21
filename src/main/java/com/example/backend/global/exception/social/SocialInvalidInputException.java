package com.example.backend.global.exception.social;

import com.example.backend.global.exception.base.CustomException;
import com.example.backend.global.exception.base.CustomExceptionType;

public class SocialInvalidInputException extends CustomException {

    private SocialInvalidInputExceptionType exceptionType;

    public SocialInvalidInputException(SocialInvalidInputExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public CustomExceptionType getExceptionType() {
        return exceptionType;
    }
}
