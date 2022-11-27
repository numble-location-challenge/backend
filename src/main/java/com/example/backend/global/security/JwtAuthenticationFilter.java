package com.example.backend.global.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *  antMatchers 경로 제외하고 인증 필요한 모든 요청 여기로 들어옴
 * 검증에는 Access Token 만 필요
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private String TOKEN_PREFIX = "Bearer ";

    private List<String> NOT_CHECK_URL = List.of("/login", "/join", "/refresh", "/swagger-ui");

    private final TokenService tokenService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        for(String notUrl : NOT_CHECK_URL){
            if(path.startsWith(notUrl)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if(shouldNotFilter(request)){
            filterChain.doFilter(request,response);
            return;
        }

        log.info("JWT Filter is running...");
        // 요청에서 토큰 가져오기
        String token = parseBearerToken(request);
        if(token != null && !token.equalsIgnoreCase("null")){
            try{
                //토큰 검증 (jwt이므로 인가서버에 요청하지 않고도 검증 가능)
                JwtSubject jwtSubject = tokenService.validateAndGetSubject(token);
                //토큰 타입 검증
                if(jwtSubject != null && !jwtSubject.getJwtType().equals(JwtType.ACCESS)) throw new JwtException("Access Token type 이 아닙니다");
                //사용자 인증정보 저장
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        jwtSubject.getEmail(),
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );
                // SecurityContextHolder에 등록(요청 끝날때까지 들고있어야함)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            } catch (JwtException ex){
                log.info("유효기간이 만료되었거나 구조가 잘못되었거나 파싱 실패한 토큰입니다.", ex);
                throw new AuthenticationException("AuthenticationException 에러");
            }
        }

        filterChain.doFilter(request, response);
    }

    public String parseBearerToken(HttpServletRequest request) {
        // http 헤더 파싱해 토큰 얻음
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        //AccessToken인지 검증, 토큰 파싱
        if(StringUtils.hasText(authorization) && authorization.startsWith(TOKEN_PREFIX)){
            return authorization.replace(TOKEN_PREFIX,"");
        }
        return null;
    }
}

