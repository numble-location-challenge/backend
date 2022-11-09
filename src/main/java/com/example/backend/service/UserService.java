package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.UserJoinRequestDTO;
import com.example.backend.dto.UserModifyRequestDTO;

public interface UserService {

    void create(UserJoinRequestDTO userDTO); //회원가입

    void delete(Long userId); //회원 탈퇴

    User getUser(Long userId); //내 프로필 조회

    User modifyUser(Long userId, UserModifyRequestDTO userModifyRequestDTO); //회원 수정

}
