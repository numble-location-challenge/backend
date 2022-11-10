package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomException;
import com.example.backend.global.exception.base.CustomExceptionType;

public class InvalidInputException extends CustomException {

    private InvalidInputExceptionType exceptionType;

    public InvalidInputException(InvalidInputExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public CustomExceptionType getExceptionType() {
        return exceptionType;
    }
}
