package com.example.backend.global.security;

import com.example.backend.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

//TODO: 리팩토링 예정
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private static final String SECRET_KEY = "NMA8JPctFuna59f5NMA8JPctFuna59f5NMA8JPctFuna59f5NMA8JPctFuna59f5NMA8JPctFuna59f5NMA8JPctFuna59f5";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String createAccessToken(User user) {
        // 기한 1시간으로 설정
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.HOURS));

        // JWT Token 생성
        return Jwts.builder()
                // header 내용 및 SECRET_KEY 세팅
                .signWith(key, SignatureAlgorithm.HS512) //알고리즘
                // payload
                .setSubject(user.getId().toString()) // sub
                .setIssuer("Numble backend") // iss
                .setIssuedAt(new Date()) // iat 현재시간으로 생성
                .setExpiration(expiryDate) // exp
                .compact();
    }

    public String createRefreshToken(User user) {
        // 기한 한 달로 설정
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.MONTHS));

        // JWT Token 생성
        return Jwts.builder()
                // header 내용 및 SECRET_KEY 세팅
                .signWith(key, SignatureAlgorithm.HS512) //알고리즘
                // payload
                .setSubject(user.getEmail()) // sub
                .setIssuer("Numble backend") // iss
                .setIssuedAt(new Date()) // iat 현재시간으로 생성
                .setExpiration(expiryDate) // exp
                .compact();
    }

    //jwt 검증 후 아이디(이메일) 추출
    public String validateAndGetUserEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)// token을 Base 64로 디코딩 및 파싱
                .getBody(); // get userId(payload(Claims))

        return claims.getSubject();
    }

    public void destroyToken(String email) {

    }
}
