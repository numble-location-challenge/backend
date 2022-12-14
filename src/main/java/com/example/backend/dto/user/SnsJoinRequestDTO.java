package com.example.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema
public class SnsJoinRequestDTO {

    @Schema(description = "이름", defaultValue = "송유진")
    @NotBlank
    private String username;

    @Schema(description = "핸드폰 번호", defaultValue = "010-1234-5678")
    @NotBlank
    private String phoneNumber;

    @Schema(description = "지역 번호", defaultValue = "1111010400")
    @NotNull
    Long dongCode;

    @Schema(description = "행정구역명", defaultValue = "서울특별시 종로구 효자동")
    @NotNull
    String dongName;
}
