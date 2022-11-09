package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.UserJoinRequestDTO;
import com.example.backend.dto.UserModifyRequestDTO;

public interface UserService {

    void create(UserJoinRequestDTO userDTO); //회원가입

    void delete(Long userId); //회원 탈퇴

    User getUserProfile(Long userId); //내 프로필 조회

    User modifyUserProfile(UserModifyRequestDTO userModifyDTO); //회원 수정

}
