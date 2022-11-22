package com.example.backend.global.security;

import com.example.backend.domain.User;
import com.example.backend.global.exception.UnAuthorizedException;
import com.example.backend.global.exception.UnAuthorizedExceptionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.access.expiration}")
    private Long ACCESS_EXP;

    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_EXP;

    private final ObjectMapper objectmapper;

    public Key getKeyForSign(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String issueAccessToken(User user) {
        String subject = getTokenSubjectStr(user, JwtType.ACCESS);
        Date expiryDate = Date.from(
                Instant.now().plusSeconds(ACCESS_EXP));

        return createToken(subject, expiryDate);
    }

    public String issueRefreshToken(User user) {
        String subject = getTokenSubjectStr(user, JwtType.REFRESH);
        Date expiryDate = Date.from(
                Instant.now().plusSeconds(REFRESH_EXP));

        return createToken(subject, expiryDate);
    }

    private String createToken(String subject, Date expiryDate) {
        // JWT Token 생성
        return Jwts.builder()
                // header 내용 및 SECRET_KEY 세팅
                .signWith(getKeyForSign(), SignatureAlgorithm.HS512) //알고리즘
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
            throw new UnAuthorizedException(UnAuthorizedExceptionType.PARSING_FAIL);
        }
    }

    //jwt 검증 후 아이디(이메일) 추출
    public JwtSubject validateAndGetSubject(String token){ //getSubjects
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getKeyForSign())
                    .build()
                    .parseClaimsJws(token)// token을 Base 64로 디코딩 및 파싱
                    .getBody(); // get userId(payload(Claims))

            return objectmapper.readValue(claims.getSubject(), JwtSubject.class);
        } catch(SignatureException | MalformedJwtException e){ //서명 또는 구조 문제
            throw new UnAuthorizedException(UnAuthorizedExceptionType.INVALID_TOKEN);
        } catch (ExpiredJwtException e){ //유효기간이 만료된 토큰
            throw new UnAuthorizedException(UnAuthorizedExceptionType.REFRESH_TOKEN_IS_EXPIRED);
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
            e.printStackTrace();
            throw new UnAuthorizedException(UnAuthorizedExceptionType.PARSING_FAIL);
        }
    }

    public void destroyToken(String email, String refreshToken) {
        //TODO

    }

}
