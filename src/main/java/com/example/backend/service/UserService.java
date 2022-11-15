package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.login.KaKaoAuthRequestDTO;
import com.example.backend.dto.user.UserJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;

public interface UserService {

    User createDefaultUser(UserJoinRequestDTO userDTO); //회원가입

    User createKakaoUser(KaKaoAuthRequestDTO authRequestDTO);

    void delete(Long userId); //회원 탈퇴

    User getUser(Long userId); //내 프로필 조회

    User modifyUser(Long userId, UserModifyRequestDTO userModifyRequestDTO); //회원 수정

    User getKakaoUserInfo(String KakaoAccessToken);
}
