package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.login.KaKaoAuthRequestDTO;
import com.example.backend.dto.user.UserJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;

public interface UserService {

    User createDefaultUser(UserJoinRequestDTO userDTO); //회원가입

    User createKakaoUser(KaKaoAuthRequestDTO authRequestDTO);

    void delete(String email); //회원 탈퇴

    User getUser(String email, Long id); //내 프로필 조회

    User getKakaoUserInfo(String KakaoAccessToken);

    User modify(UserModifyRequestDTO userModifyRequestDTO, String email);
}
