package com.example.backend.dto.user;

import com.example.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class UserProfileDTO {

    @Schema(description = "프로필 사진", defaultValue = "조회 또는 변경된 프사 경로")
    private String profile;

    @Schema(description = "닉네임", defaultValue = "조회 또는 변경된 닉네임")
    private String nickname;

    @Schema(description = "짧은 소개글", defaultValue = "조회 또는 변경된 소개글")
    private String bio;

    @Schema(description = "지역 번호 8자리", defaultValue = "1111010400")
    private Integer dongCode;

    @Schema(description = "행정구역명", defaultValue = "서울특별시 종로구 효자동")
    private String dongName;

    public UserProfileDTO(User user) {
        this.nickname = user.getNickname();
        this.profile = user.getProfile();
        this.dongCode = user.getDongCode();
        this.dongName = user.getDongName();
        this.bio = user.getBio();
    }
}
