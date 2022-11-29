package com.example.backend.dto.user;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import lombok.Builder;
import lombok.Data;

@Data
public class SnsUserDTO {
    //API에서 받아서 채워넣음
    private Long id;//id, 사용자의 카카오 회원번호
    private String nickname; //필수동의 카카오닉네임
    private String profile; //필수동의
    private String email; //선택동의, 예외처리

    @Builder
    public SnsUserDTO(Long id, String nickname, String profile, String email) {
        this.id = id;
        this.nickname = nickname;
        this.profile = profile;
        this.email = email;
    }

    public User toEntity(UserType usertype, String username, String phoneNumber, Long dongCode, String dongName) {
        return User.builder()
                .userType(usertype).email(email).nickname(nickname)
                .username(username).phoneNumber(phoneNumber)
                .dongCode(dongCode).dongName(dongName)
                .build();
    }
}
