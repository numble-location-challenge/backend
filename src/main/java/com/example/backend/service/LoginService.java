package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.login.DefaultLoginRequestDTO;
import com.example.backend.dto.login.SocialLoginRequestDTO;

import java.util.HashMap;

public interface LoginService {

    User defaultLogin(DefaultLoginRequestDTO loginDTO);

    User kakaoLogin(SocialLoginRequestDTO authRequestDTO);

    HashMap<String,String> getAccessAndRefreshToken(User user);

    void logout(String email, String refreshToken);

    User getUserByRefreshToken(String refreshToken);

    String refresh(User user, String refreshToken);
}
