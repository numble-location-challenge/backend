package com.example.backend.dto;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class UserJoinDTO {

    @Schema(description = "회원 타입", defaultValue = "DEFAULT")
    @NotNull
    private UserType userType; // 기본 회원, 카카오 회원 구분

    @Schema(description = "이메일(아이디)", defaultValue = "testId")
    @NotNull
    private String email; // 아이디

    @Schema(description = "비밀번호", defaultValue = "testPw")
    @NotNull
    private String password;

    @Schema(description = "이름", defaultValue = "송유진")
    @NotNull
    private String username;

    @Schema(description = "닉네임", defaultValue = "부챠라티")
    @NotNull
    private String nickname;

    @Schema(description = "핸드폰 번호", defaultValue = "010-1234-5678")
    @NotNull
    private String phoneNumber;

    @Schema(description = "지역 번호(동)", defaultValue = "157")
    @NotNull
    private int region;

    public User toEntity(){
        return User.builder()
                .userType(userType).email(email).password(password)
                .username(username).nickname(nickname).phoneNumber(phoneNumber).region(region)
                .build();
    }
}
