package com.example.backend.service.login;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.DefaultLoginRequestDTO;
import com.example.backend.global.security.jwt.authToken.AuthToken;

public interface LoginService {

    User defaultLogin(DefaultLoginRequestDTO loginDTO);

    User snsLogin(UserType userType, String accessToken);

    AuthToken refresh(Long userId, AuthToken refreshToken);

    void updateRefresh(User loginUser, AuthToken refreshToken);
}
