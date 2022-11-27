package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.DefaultLoginRequestDTO;
import com.example.backend.dto.login.SocialLoginRequestDTO;

public interface LoginService {

    User defaultLogin(DefaultLoginRequestDTO loginDTO);

    User snsLogin(UserType userType, SocialLoginRequestDTO authRequestDTO);

    void logout(String email, String refreshToken);

    User getUserByRefreshToken(String refreshToken);

    String refresh(User user, String refreshToken);

    void updateRefresh(User loginUser, String refreshToken);
}
