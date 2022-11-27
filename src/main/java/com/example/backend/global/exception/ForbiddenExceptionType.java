package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomExceptionType;

/**
 * HttpStatus: FORBIDDEN
 */
public enum ForbiddenExceptionType implements CustomExceptionType {
    USER_UN_AUTHORIZED(-199, "권한이 없는 페이지입니다."),
    NOT_AUTHORITY_DELETE_FEED(-202,"피드를 삭제할 권한이 없습니다."),
    NOT_AUTHORITY_UPDATE_FEED(-203, "피드를 수정할 권한이 없습니다." ),
    NOT_AUTHORITY_UPDATE_COMMENT(-402, "댓글을 수정할 권한이 없습니다."),
    NOT_AUTHORITY_DELETE_COMMENT(-403, "댓글을 삭제할 권한이 없습니다.");

    private int errorCode;
    private String errorMessage;

    ForbiddenExceptionType(int errorCode, String errorMessage) {
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
