package com.example.backend.service.user;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.SnsJoinRequestDTO;
import com.example.backend.dto.user.SnsUserDTO;
import org.springframework.transaction.annotation.Transactional;


public interface SnsUserService {

    Long getUserId(UserType userType, String accessToken);

    SnsUserDTO getUserInfo(UserType userType, String accessToken);

    @Transactional
    User createSnsUser(UserType userType, SnsJoinRequestDTO joinDTO);

    //Test
//    TestKakaoToken requestToken(String code) throws IOException;
}
