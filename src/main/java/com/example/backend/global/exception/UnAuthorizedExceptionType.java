package com.example.backend.global.exception;

import com.example.backend.global.exception.base.CustomExceptionType;

/**
 * HttpStatus: UNAUTHORIZED
 */
public enum UnAuthorizedExceptionType implements CustomExceptionType {
    ACCESS_TOKEN_UN_AUTHORIZED(-188, "Access Token이 없거나 유효하지 않습니다. '/refresh' 으로 재요청 하십시오."),
    REFRESH_TOKEN_UN_AUTHORIZED(-177, "Refresh Token이 없거나 유효하지 않습니다. '/login' 으로 재요청 하십시오."),
    PARSING_FAIL(-155, "토큰 파싱에 실패했습니다."),
    API_REQUEST_FAIL(-144, "SNS API와의 통신에 실패했습니다.");

    private int errorCode;
    private String errorMessage;

    UnAuthorizedExceptionType(int errorCode, String errorMessage) {
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
