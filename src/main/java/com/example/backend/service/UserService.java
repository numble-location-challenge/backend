package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.SocialJoinRequestDTO;
import com.example.backend.dto.user.UserDefaultJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;

public interface UserService {

    User createDefaultUser(UserDefaultJoinRequestDTO userDTO); //회원가입

    User createSnsUser(UserType userType, SocialJoinRequestDTO authRequestDTO);

    void changeToWithdrawnUser(String email); //회원 탈퇴

    User getUser(String email, Long id); //내 프로필 조회

    User modify(String email, Long userId, UserModifyRequestDTO userModifyRequestDTO);

    //모임신청
    void participateSocial(String email, Long socialId);

    //취소
    void cancelSocialParticipation(String email, Long socialId);

    //강퇴
    void kickOutUserFromSocial(String email, Long socialId, Long droppedUserId);
}
