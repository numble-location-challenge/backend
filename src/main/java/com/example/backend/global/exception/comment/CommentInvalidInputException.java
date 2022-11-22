package com.example.backend.global.exception.comment;

import com.example.backend.global.exception.base.CustomException;
import com.example.backend.global.exception.base.CustomExceptionType;

public class CommentInvalidInputException extends CustomException {

    private CommentInvalidInputExceptionType exceptionType;

    public CommentInvalidInputException(CommentInvalidInputExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public CustomExceptionType getExceptionType() {
        return exceptionType;
    }
}
