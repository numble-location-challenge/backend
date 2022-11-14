package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.login.KaKaoAuthRequestDTO;

import java.util.HashMap;

public interface LoginService {

    User defaultLogin(String email, String password);

    User kakaoLogin(KaKaoAuthRequestDTO authRequestDTO);

    User getKakaoUserInfo(String KakaoAccessToken);

    HashMap<String,String> authorize(User user);

    void logout(String email);
}
