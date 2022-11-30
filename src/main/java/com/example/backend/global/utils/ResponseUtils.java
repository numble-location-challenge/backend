package com.example.backend.global.utils;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.login.AuthDTO;
import com.example.backend.global.security.AuthToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class ResponseUtils {

    public static ResponseEntity<?> getLoginSuccessResponse(Long userId, AuthToken AT, ResponseCookie RTCookie , String message) {
        //set response
        AuthDTO authDTO = new AuthDTO(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, HttpHeaders.AUTHORIZATION)
                .header(HttpHeaders.AUTHORIZATION, AT.getToken())
                .header(HttpHeaders.SET_COOKIE, RTCookie.toString())
                .body(new ResponseDTO<>(authDTO, message));
    }

    public static ResponseEntity<?> getCreatedUserResponse(UriComponentsBuilder uriBuilder, Long id) {
        URI location = uriBuilder.path("/user/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location)
                .body(new ResponseDTO<>(null, "회원가입 처리되었습니다."));
    }
}
