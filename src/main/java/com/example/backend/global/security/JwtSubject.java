package com.example.backend.global.security;

import io.jsonwebtoken.Jwt;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtSubject {

    private Long userId; //pk
    private String email; //id
    private JwtType jwtType;

    public JwtSubject(Long userId, String email, JwtType jwtType) {
        this.userId = userId;
        this.email = email;
        this.jwtType = jwtType;
    }
}
