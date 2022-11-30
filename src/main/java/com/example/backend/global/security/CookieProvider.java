package com.example.backend.global.security;

import com.example.backend.global.security.jwt.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieProvider {

    private final JwtConfig jwtConfig;

    private String REFRESH_COOKIE = "refreshToken";

    public ResponseCookie createRefreshTokenCookie(String refreshToken){
        deleteRefreshTokenCookie();

        return ResponseCookie.from(REFRESH_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(false) //TODO SSL 인증서 필요해서 나중에
                .sameSite("None")
                .maxAge(jwtConfig.getRefreshExpiry())
                .path("/")
                .build();
    }

    public void deleteRefreshTokenCookie(){
        ResponseCookie.from(REFRESH_COOKIE, "")
                .maxAge(1)
                .build();
    }
}
