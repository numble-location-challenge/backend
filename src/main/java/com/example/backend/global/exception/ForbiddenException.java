package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomException;
import com.example.backend.global.exception.base.CustomExceptionType;

public class ForbiddenException extends CustomException {

    private ForbiddenExceptionType exceptionType;

    public ForbiddenException(ForbiddenExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public CustomExceptionType getExceptionType() {
        return exceptionType;
    }
}
