package com.example.backend.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class AuthDTO {
    @Schema(description = "회원 pk", defaultValue = "1")
    Long userId;

    @Schema(description = "회원 이메일(아이디)", defaultValue = "hello@numble.com")
    String email;
}
