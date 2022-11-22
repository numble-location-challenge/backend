package com.example.backend.dto.social;

import com.example.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Getter;

@Schema
@Getter
@Builder
public class SocialUserDTO {
    @Schema(description = "게시글을 작성한 유저 아이디", defaultValue = "1")
    private Long id;

    @Schema(description = "게시글을 작성한 유저 닉네임", defaultValue = "철수철수김")
    private String name;

}
