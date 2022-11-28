package com.example.backend.service.user;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.SnsJoinRequestDTO;
import com.example.backend.dto.user.UserDefaultJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;

public interface UserService {

    User createDefaultUser(UserDefaultJoinRequestDTO userDTO); //회원가입

    void changeToWithdrawnUser(String email, Long userId); //회원 탈퇴

    User getUser(String email, Long userId); //내 프로필 조회

    User modify(String email, Long userId, UserModifyRequestDTO userModifyRequestDTO);

    //모임신청
    void participateSocial(String email, Long socialId);

    //취소
    void cancelSocialParticipation(String email, Long socialId);

    //강퇴
    void kickOutUserFromSocial(String email, Long socialId, Long droppedUserId);
}
