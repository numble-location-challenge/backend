package com.example.backend.dto.user;

import com.example.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema
@NotNull
public class UserDefaultJoinRequestDTO {

    @Schema(description = "이메일(아이디)", defaultValue = "hello@numble.com")
    @NotBlank
    private String email; // 아이디

    @Schema(description = "비밀번호", defaultValue = "testPw")
    @NotBlank
    private String password;

    @Schema(description = "이름", defaultValue = "송유진")
    @NotBlank
    private String username;

    @Schema(description = "닉네임", defaultValue = "부챠라티")
    @NotBlank
    private String nickname;

    @Schema(description = "핸드폰 번호", defaultValue = "010-1234-5678")
    @NotBlank
    private String phoneNumber;

    @Schema(description = "짧은 소개글", defaultValue = "파리&제주스냅 셔터프레소📷\n 청춘스냅 잘하기로 소문난 집")
    private String bio;

    @Schema(description = "지역 번호(동)", defaultValue = "157")
    @NotNull
    private int region;

    public User toEntity(){
        return User.builder()
                .email(email).password(password).username(username).nickname(nickname)
                .phoneNumber(phoneNumber).region(region).bio(bio)
                .build();
    }
}
