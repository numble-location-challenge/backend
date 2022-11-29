package com.example.backend.global.security;

import com.example.backend.domain.User;
import com.example.backend.global.exception.UnAuthorizedException;
import com.example.backend.global.exception.UnAuthorizedExceptionType;
import com.example.backend.global.security.jwt.JwtType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
public class AuthTokenProvider {

    @Value("${jwt.access.expiration}")
    private Long ACCESS_EXP;

    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_EXP;

    private Key key;

    public AuthTokenProvider(String secret){
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public AuthToken createAuthToken(String id, Date expiry) {
        return new AuthToken(key, id, expiry);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public AuthToken issueAccessToken(User user) {
        String subject = getTokenSubjectStr(user, JwtType.ACCESS);
        Date expiryDate = Date.from(
                Instant.now().plusSeconds(ACCESS_EXP));

        AuthToken authToken = createAuthToken(subject, expiryDate);
        log.info("issueAccessToken.AuthToken.getToken(): {} ", authToken.getToken());
        return authToken;
    }

    public AuthToken issueRefreshToken(User user) {
        String subject = getTokenSubjectStr(user, JwtType.REFRESH);
        Date expiryDate = Date.from(
                Instant.now().plusSeconds(REFRESH_EXP));

        AuthToken authToken = createAuthToken(subject, expiryDate);
        log.info("issueRefreshToken.AuthToken.getToken(): {} ", authToken.getToken());
        return authToken;
    }

    /**
     * 토큰 생성 시 호출되는 메서드
     * 토큰 타입에 맞는 CustomUserDetails 객체를 생성해서 String으로 변환해 리턴
     * @param user
     * @param jwtType
     * @return String
     */
    private String getTokenSubjectStr(User user, JwtType jwtType) {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(new CustomUserDetails(user.getId(), user.getEmail(), jwtType));
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
            throw new UnAuthorizedException(UnAuthorizedExceptionType.PARSING_FAIL);
        }
    }

    /**
     * jwt 검증 후 아이디(이메일) 추출
     * 여기서 JwtException 발생하면 호출한 곳으로 돌아가므로, 호출한 곳(filter)에서는 예외처리 필수
     * @param token
     * @return
     */
    public UsernamePasswordAuthenticationToken getAuthentication(AuthToken authToken) throws JsonProcessingException {
        if(authToken.validate()){
            Claims claims = authToken.getClaimsFromToken(); //에러 발생 가능
            CustomUserDetails userDetails = CustomUserDetails.createUserDetails(claims.getSubject());
            return new UsernamePasswordAuthenticationToken(userDetails, null, AuthorityUtils.NO_AUTHORITIES);
        } else {
            return null;
        }
    }

}
