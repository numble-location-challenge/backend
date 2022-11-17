package com.example.backend.dto.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema
@Getter
@Builder
public class SocialUserDTO {
    @Schema(description = "유저 아이디")
    private Long id;
    @Schema(description = "유저 닉네임")
    private String name;
}
