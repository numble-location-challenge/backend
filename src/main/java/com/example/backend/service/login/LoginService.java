package com.example.backend.service.login;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.DefaultLoginRequestDTO;
import com.example.backend.dto.login.SnsLoginRequestDTO;

public interface LoginService {

    User defaultLogin(DefaultLoginRequestDTO loginDTO);

    User snsLogin(UserType userType, SnsLoginRequestDTO authRequestDTO);

    User getUserByRefreshToken(String refreshToken);

    String refresh(User user, String refreshToken);

    void updateRefresh(User loginUser, String refreshToken);
}
