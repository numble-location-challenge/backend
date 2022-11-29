package com.example.backend.service.login;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.DefaultLoginRequestDTO;
import com.example.backend.dto.login.SnsLoginRequestDTO;
import com.example.backend.global.security.AuthToken;

public interface LoginService {

    User defaultLogin(DefaultLoginRequestDTO loginDTO);

    User snsLogin(UserType userType, SnsLoginRequestDTO authRequestDTO);

//    User getUserByRefreshToken(String refreshToken);

    AuthToken refresh(Long userId, AuthToken refreshToken);

    void updateRefresh(User loginUser, AuthToken refreshToken);
}
