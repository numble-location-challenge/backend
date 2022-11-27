package com.example.backend.service;

import com.example.backend.controller.TestKakaoToken;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.user.SnsUserDTO;

import java.io.IOException;

public interface KakaoService {

    String getUserEmail(UserType userType, String accessToken);

    SnsUserDTO getUserInfo(UserType userType, String accessToken);

//    TestKakaoToken requestToken(String code) throws IOException;
}
