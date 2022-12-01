package com.example.backend.global.security.jwt.authToken;

import com.example.backend.global.security.CustomUserDetails;
import com.example.backend.global.security.jwt.JwtType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
@Slf4j
public class AuthToken {

    private final String token;
    private final Key key;

    AuthToken(Key key, String subject, Date expiry){
        this.key = key;
        this.token = createAuthToken(subject, expiry);
    }

    private String createAuthToken(String subject, Date expiry) {
        // JWT Token 생성
        String token = Jwts.builder()
                // header 내용 및 SECRET_KEY 세팅
                .signWith(key, SignatureAlgorithm.HS512) //알고리즘
                // payload
                .setClaims(Jwts.claims().setSubject(subject)) // sub
                .setIssuer("Numble backend") // iss
                .setIssuedAt(new Date()) // iat 현재시간으로 생성
                .setExpiration(expiry) // exp
                .compact();

        log.info("createAuthToken: {}", token);
        return token;
    }

    //검증
    public boolean validate(){
        return getClaimsFromToken() != null;
    }

    public <T> T getClaimFromToken(Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken();
        return claimsResolver.apply(claims);
    }

    public Long getUserIdFromClaims() {
        String subjectStr = getClaimFromToken(Claims::getSubject);
        ObjectMapper om = new ObjectMapper();
        CustomUserDetails userDetails = null;
        try{
            userDetails = om.readValue(subjectStr, CustomUserDetails.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userDetails.getUserId();
    }

    public JwtType getTokenTypeFromClaims(){
        String subjectStr = getClaimFromToken(Claims::getSubject);
        ObjectMapper om = new ObjectMapper();
        CustomUserDetails userDetails = null;
        try{
            userDetails = om.readValue(subjectStr, CustomUserDetails.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userDetails.getJwtType();
    }

    //여기서 발생하는 에러는 호출된 곳으로 돌아가고 JwtExceptionEntiryPoint에서 잡힌다
    public Claims getClaimsFromToken(){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }  catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    //만료됐는지 확인 -> AccessToken가 같이 온다면 확인
    public Claims getExpiredTokenClaims() {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            return e.getClaims();
        }
        return null;
    }

}
