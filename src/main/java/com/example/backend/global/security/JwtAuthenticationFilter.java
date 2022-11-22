package com.example.backend.global.security;

import com.example.backend.global.exception.UnAuthorizedException;
import com.example.backend.global.exception.UnAuthorizedExceptionType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * /login 제외 인증 필요한 모든 요청 여기로 들어옴
 * 검증에는 Access Token 만 필요
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private String TOKEN_PREFIX = "Bearer ";

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            // 요청에서 토큰 가져오기
            String token = parseBearerToken(request);
            log.info("JWT Filter is running...");

            if(token != null && !token.equalsIgnoreCase("null")){
                JwtSubject jwtSubject = tokenService.validateAndGetSubject(token);
                // subject type 검증
                if(!jwtSubject.getJwtType().equals(JwtType.ACCESS)) throw new JwtException("Access Token type 이 아닙니다");
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
            }else{
                log.info("토큰이 null입니다.");
            }
        } catch (JwtException e){
            log.info("Could not set user authentication in security context", e);
            throw new UnAuthorizedException(UnAuthorizedExceptionType.ACCESS_TOKEN_UN_AUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        // http 헤더 파싱해 토큰 얻음
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        //AccessToken인지 검증, 토큰 파싱
        if(StringUtils.hasText(authorization) && authorization.startsWith(TOKEN_PREFIX)){
            return authorization.replace(TOKEN_PREFIX,"");
        }
        return null;
    }
}

