package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomException;
import com.example.backend.global.exception.base.CustomExceptionType;

public class UnAuthorizedException extends CustomException {

    private UnAuthorizedExceptionType exceptionType;

    public UnAuthorizedException(UnAuthorizedExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public CustomExceptionType getExceptionType() {
        return exceptionType;
    }
}
