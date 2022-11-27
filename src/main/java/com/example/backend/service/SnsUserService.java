package com.example.backend.service;

import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.user.SnsUserDTO;


public interface SnsUserService {

    String getUserEmail(UserType userType, String accessToken);

    SnsUserDTO getUserInfo(UserType userType, String accessToken);

    //Test
//    TestKakaoToken requestToken(String code) throws IOException;
}
