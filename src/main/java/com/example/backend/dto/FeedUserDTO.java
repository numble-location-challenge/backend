package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema
@Getter
@Builder
public class FeedUserDTO {

    @Schema(description = "유저의 아이디", defaultValue = "5")
    private Long id;
    @Schema(description = "유저의 프로필이미지 경로", defaultValue = "6264d-5324-cdcd.jpg")
    private String profile;
    @Schema(description = "유저의 닉네임", defaultValue = "닉네임1")
    private String nickname;
}
