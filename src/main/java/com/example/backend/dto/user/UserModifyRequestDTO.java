package com.example.backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema
public class UserModifyRequestDTO {

    @Schema(description = "프로필 사진", defaultValue = "/file/profile/selfie.jpeg")
    String profile;

    @Schema(description = "닉네임", defaultValue = "부챠라티222")
    String username;

    @Schema(description = "짧은 소개글", defaultValue = "y o u j i n\n지금은 #INTP\n인생의 특별한 퍼즐 조각을 맞춰가는 중🧩")
    String bio;

    @Schema(description = "지역 번호(동)", defaultValue = "903")
    Integer region;
}
