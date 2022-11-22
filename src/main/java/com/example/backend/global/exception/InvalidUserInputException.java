package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomException;
import com.example.backend.global.exception.base.CustomExceptionType;

public class InvalidUserInputException extends CustomException {

    private InvalidUserInputExceptionType exceptionType;

    public InvalidUserInputException(InvalidUserInputExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public CustomExceptionType getExceptionType() {
        return exceptionType;
    }
}
