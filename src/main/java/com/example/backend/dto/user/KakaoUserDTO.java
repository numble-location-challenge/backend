package com.example.backend.dto.user;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import lombok.Data;

import java.util.UUID;

@Data
public class KakaoUserDTO {
    private Long id;//id, 사용자의 카카오 회원번호

    //아래 정보들은 KakaoAccount 타입에서 받아오는 것
    private String name; //동의 필요, name_needs_agreement가 true
    private String email; //동의 필요, 예외처리 복잡

    //로그인 하는 사용자에 따라 전화번호가 없는 계정은 동의 항목이 설정되어 있어도 전화번호를 조회할 수 없다
    private String phoneNumber; //phone_number

    //카카오 이메일은 값이 변할 수 있어서 아이디로 쓰기 적절하지 X
    public User toEntity(int region) {
        User user =  User.builder()
                .userType(UserType.KAKAO).email(email).username(name).nickname(UUID.randomUUID().toString())
                .phoneNumber(phoneNumber).region(region)
                .build();
        user.setId(id);
        return user;
    }
}
