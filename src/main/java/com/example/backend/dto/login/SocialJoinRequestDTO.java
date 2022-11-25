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
public class SocialJoinRequestDTO {
    @Schema(description = "정보를 조회할 SNS API의 액세스 토큰")
    @NotNull
    String accessToken;

    @Schema(description = "지역 번호 8자리", defaultValue = "1111010400")
    @NotNull
    int regionCode;

    @Schema(description = "행정구역명", defaultValue = "서울특별시 종로구 효자동")
    @NotNull
    String regionName;
}
