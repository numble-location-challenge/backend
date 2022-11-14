package com.example.backend.global.security;

import io.jsonwebtoken.Jwt;
import lombok.Getter;

@Getter
public class JwtSubject {

    private final Long userId; //pk
    private final String email; //id
    private final JwtType jwtType;

    public JwtSubject(Long userId, String email, JwtType jwtType) {
        this.userId = userId;
        this.email = email;
        this.jwtType = jwtType;
    }
}
