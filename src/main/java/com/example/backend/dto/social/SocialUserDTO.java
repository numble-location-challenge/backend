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
    @Schema(description = "유저 아이디", defaultValue = "1")
    private Long id;

    @Schema(description = "유저 닉네임", defaultValue = "tester")
    private String name;

//    public SocialUserDTO(User user){
//        this.id = user.getId();
//        this.name = user.getNickname();
//    }
}
