package com.example.backend.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema
public class DefaultLoginRequestDTO {

    @Schema(description = "이메일", defaultValue = "hello@numble.com")
    @NotNull
    String email;

    @Schema(description = "비밀번호", defaultValue = "testPw")
    @NotNull
    String password;
}
