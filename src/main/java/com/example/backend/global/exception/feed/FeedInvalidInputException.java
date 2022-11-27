package com.example.backend.global.exception.feed;

import com.example.backend.global.exception.base.CustomException;
import com.example.backend.global.exception.base.CustomExceptionType;

public class FeedInvalidInputException extends CustomException {

    private FeedInvalidInputExceptionType exceptionType;

    public FeedInvalidInputException(FeedInvalidInputExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public CustomExceptionType getExceptionType() {
        return exceptionType;
    }
}
