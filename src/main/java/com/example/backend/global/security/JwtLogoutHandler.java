package com.example.backend.global.security;

import com.example.backend.domain.User;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.global.exception.EntityNotExistsException;
import com.example.backend.global.exception.EntityNotExistsExceptionType;
import com.example.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //시큐리티 컨텍스트에서 email 가져옴
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        //email로 DB에 있는 유저인지 확인
        User findUser = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        //DB의 RT 삭제
        findUser.deleteRefreshToken();

        //set response
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ResponseDTO<?> responseDTO = ResponseDTO.builder().success(true).message("로그아웃 되었습니다.").build();
        //convert json
        try {
            ObjectMapper om = new ObjectMapper();
            String result = om.writeValueAsString(responseDTO);
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
