package com.example.backend.dto.user;

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

    @Schema(description = "지역 번호(동)", defaultValue = "조회 또는 변경된 지역번호")
    private int region;

}
