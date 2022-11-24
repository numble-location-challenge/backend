package com.example.backend.global.security;

import com.example.backend.domain.User;
import com.example.backend.global.exception.UnAuthorizedException;
import com.example.backend.global.exception.UnAuthorizedExceptionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
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

    /**
     * 토큰 생성 시 호출되는 메서드
     * 토큰 타입에 맞는 JwtSubject 객체를 생성해서 String으로 변환해 리턴
     * @param user
     * @param jwtType
     * @return String
     */
    private String getTokenSubjectStr(User user, JwtType jwtType) {
        try {
            return objectmapper.writeValueAsString(new JwtSubject(user.getId(), user.getEmail(), jwtType));
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
            throw new UnAuthorizedException(UnAuthorizedExceptionType.PARSING_FAIL);
        }
    }

    /**
     * jwt 검증 후 아이디(이메일) 추출(예외처리는 filter에서)
     * 여기서 JwtException 발생하면 호출한 곳으로 돌아가므로
     * 호출한 곳에서는 예외처리 필수
     * @param token
     * @return
     */
    public JwtSubject validateAndGetSubject(String token) { //getSubjects
        Claims claims = getAllClaimsFromToken(token); // get userId(payload(Claims))
        try {
            return objectmapper.readValue(claims.getSubject(), JwtSubject.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getKeyForSign()).build().parseClaimsJws(token).getBody();
    }

}
