package com.example.backend.global.utils;

import com.example.backend.domain.User;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.login.AuthDTO;
import com.example.backend.global.security.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class ResponseUtils {

    private final CookieUtils cookieUtils;

    public ResponseEntity<?> getLoginSuccessResponse(Long userId, AuthToken AT, AuthToken RT, String message) {
        //TODO 원래 refresh 쿠키 삭제
        //refresh token을 http only 쿠키에 담음
        ResponseCookie cookie = cookieUtils.createRefreshTokenCookie(RT.getToken());
        //set response
        AuthDTO authDTO = new AuthDTO(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, HttpHeaders.AUTHORIZATION)
                .header(HttpHeaders.AUTHORIZATION, AT.getToken())
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ResponseDTO<>(authDTO, message));
    }

    public static ResponseEntity<?> getCreatedUserResponse(UriComponentsBuilder uriBuilder, Long id) {
        URI location = uriBuilder.path("/user/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location)
                .body(new ResponseDTO<>(null, "회원가입 처리되었습니다."));
    }
}
