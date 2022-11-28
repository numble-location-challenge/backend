package com.example.backend.service.user;

import com.example.backend.controller.TestKakaoToken;
import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.SnsJoinRequestDTO;
import com.example.backend.dto.user.SnsUserDTO;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


public interface SnsUserService {

    Long getSnsId(UserType userType, String accessToken);

    SnsUserDTO getUserInfo(UserType userType, String accessToken);

    @Transactional
    User createSnsUser(UserType userType, SnsJoinRequestDTO joinDTO);

    //Test
    TestKakaoToken requestToken(String code) throws IOException;
}
