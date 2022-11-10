package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomException;
import com.example.backend.global.exception.base.CustomExceptionType;

public class ForbiddenException extends CustomException {


    @Override
    public CustomExceptionType getExceptionType() {
        return null;
    }
}
