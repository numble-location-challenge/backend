package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.UserDefaultLoginDTO;
import com.example.backend.dto.UserJoinDTO;
import com.example.backend.dto.UserKaKaoLoginDTO;
import com.example.backend.dto.UserModifyDTO;

public interface UserService {

    void create(UserJoinDTO userDTO); //회원가입

    void delete(Long userId); //회원 탈퇴

    User getUserProfile(Long userId); //내 프로필 조회

    User modifyUserProfile(UserModifyDTO userModifyDTO); //회원 수정

}
