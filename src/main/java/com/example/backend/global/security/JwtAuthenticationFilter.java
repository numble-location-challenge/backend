package com.example.backend.global.security;

import com.example.backend.global.exception.UnAuthorizedException;
import com.example.backend.global.exception.UnAuthorizedExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * 나중에 리팩토링할 예정
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            // 요청에서 토큰 가져오기
            String token = parseBearerToken(request);
            log.info("JWT Filter is running...");

            if(token != null && !token.equalsIgnoreCase("null")){
                // 토큰 검증
                String userId = tokenService.validateAndGetUserEmail(token);
                //사용자 인증정보를 저장
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, //인증된 사용자의 정보
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );
                // SecurityContextHolder에 등록(요청 끝날때까지 들고있어야함)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e){
            logger.error("Could not set user authentication in security context", e);
            throw new UnAuthorizedException(UnAuthorizedExceptionType.USER_UN_AUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        // http 헤더 파싱해 토큰 얻음
        String bearerToken = request.getHeader("Authorization");

        //토큰 파싱
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}

