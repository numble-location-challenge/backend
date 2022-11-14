package com.example.backend.global.security;

import com.example.backend.domain.User;
import com.example.backend.global.exception.UnAuthorizedException;
import com.example.backend.global.exception.UnAuthorizedExceptionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectmapper;

    public String issueAccessToken(User user) {
        String subject = getTokenSubjectStr(user, JwtType.ACCESS);

        // 기한 1시간으로 설정
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.HOURS));
        return createToken(subject, expiryDate);
    }

    public String issueRefreshToken(User user) {
        String subject = getTokenSubjectStr(user, JwtType.REFRESH);

        // 기한 한 달로 설정
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.MONTHS));

        return createToken(subject, expiryDate);
    }

    private String createToken(String subject, Date expiryDate) {
        // JWT Token 생성
        return Jwts.builder()
                // header 내용 및 SECRET_KEY 세팅
                .signWith(key, SignatureAlgorithm.HS512) //알고리즘
                // payload
                .setClaims(Jwts.claims().setSubject(subject)) // sub
                .setIssuer("Numble backend") // iss
                .setIssuedAt(new Date()) // iat 현재시간으로 생성
                .setExpiration(expiryDate) // exp
                .compact();
    }

    private String getTokenSubjectStr(User user, JwtType jwtType) {
        try {
            return objectmapper.writeValueAsString(new JwtSubject(user.getId(), user.getEmail(), jwtType));
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
            throw new UnAuthorizedException(UnAuthorizedExceptionType.ACCESS_TOKEN_UN_AUTHORIZED);
        }
    }

    //jwt 검증 후 아이디(이메일) 추출
    public JwtSubject validateAndGetSubject(String token){ //getSubjects
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)// token을 Base 64로 디코딩 및 파싱
                .getBody(); // get userId(payload(Claims))

        try {
            return objectmapper.readValue(claims.getSubject(), JwtSubject.class);
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
            throw new UnAuthorizedException(UnAuthorizedExceptionType.ACCESS_TOKEN_UN_AUTHORIZED);
        }
    }

    public void destroyToken(String email) {
        //TODO

    }

    public boolean isExpired(String refreshToken) {
        //TODO
        return false;
    }
}
