package com.example.backend.dto.login;

import com.example.backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Schema
public class AuthDTO implements Serializable {
    @Schema(description = "회원 pk")
    Long userId;

    public AuthDTO(User user) {
        this.userId = user.getId();
    }
}
