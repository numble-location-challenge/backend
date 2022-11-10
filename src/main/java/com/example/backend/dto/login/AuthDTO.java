package com.example.backend.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class AuthDTO {
    @Schema(description = "회원 pk", defaultValue = "1")
    Long userId;
}
