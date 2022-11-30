package com.example.backend.global.security.jwt;

import com.example.backend.dto.response.ErrorDTO;
import com.example.backend.global.exception.UnAuthorizedExceptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationException 발생시 호출된다
 *  -> UnAuthorizedException 으로 변환해서 응답
 */
@Component
@RequiredArgsConstructor
public class JwtExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ObjectMapper om = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ErrorDTO errorDTO = ErrorDTO.builder()
                .errorCode(UnAuthorizedExceptionType.ACCESS_TOKEN_UN_AUTHORIZED.getErrorCode())
                .errorMessage(UnAuthorizedExceptionType.ACCESS_TOKEN_UN_AUTHORIZED.getMessage())
                .build();
            //to json
            String result = om.writeValueAsString(errorDTO);
            response.setStatus(401);
            response.getWriter().write(result);
    }
}
