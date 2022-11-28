package com.example.backend.global.security.jwt;

import com.example.backend.global.security.AuthTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class JwtConfig {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Bean
    public AuthTokenProvider jwtProvider(){
        return new AuthTokenProvider(SECRET_KEY);
    }
}
