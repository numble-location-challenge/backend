package com.example.backend.global.exception.user;

import com.example.backend.global.exception.base.CustomException;
import com.example.backend.global.exception.base.CustomExceptionType;

public class UserInvalidInputException extends CustomException {

    private UserInvalidInputExceptionType exceptionType;

    public UserInvalidInputException(UserInvalidInputExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public CustomExceptionType getExceptionType() {
        return exceptionType;
    }
}
