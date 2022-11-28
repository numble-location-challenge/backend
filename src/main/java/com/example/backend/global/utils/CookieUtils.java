package com.example.backend.global.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_EXP;

    private String REFRESH_COOKIE = "refreshToken";

    public ResponseCookie createRefreshTokenCookie(String refreshToken){
        return ResponseCookie.from(REFRESH_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(false) //TODO SSL 인증서 필요해서 나중에
                .sameSite("None")
                .maxAge(REFRESH_EXP)
                .path("/refresh")
                .build();
    }

    //TODO 쿠키 삭제
}
