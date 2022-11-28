package com.example.backend.global.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class JwtConfig {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Bean
    public TokenService jwtProvider(){
        return new TokenService(SECRET_KEY);
    }
}
