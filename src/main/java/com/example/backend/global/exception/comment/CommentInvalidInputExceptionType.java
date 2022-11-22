package com.example.backend.global.exception.comment;

import com.example.backend.global.exception.base.CustomExceptionType;

public enum CommentInvalidInputExceptionType implements CustomExceptionType {
    INVALID_INPUT_COMMENT_ID(-404, "대댓글은 댓글에만 달 수 있습니다.");

    private int errorCode;
    private String errorMessage;

    CommentInvalidInputExceptionType(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }
}
