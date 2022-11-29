package com.example.backend.service.user;

import com.example.backend.domain.User;
import com.example.backend.dto.user.UserDefaultJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;

public interface UserService {

    User createDefaultUser(UserDefaultJoinRequestDTO userDTO); //회원가입

    void changeToWithdrawnUser(Long userId); //회원 탈퇴

    User getUser(Long userId); //내 프로필 조회

    User modify(Long userId, UserModifyRequestDTO userModifyRequestDTO);

    //==Socialing==//
    void participateSocial(Long userId, Long socialId);

    void cancelSocialParticipation(Long userId, Long socialId);

    void kickOutUserFromSocial(Long userId, Long socialId, Long droppedUserId);
}
