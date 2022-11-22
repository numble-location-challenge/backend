package com.example.backend.dto.social;

import com.example.backend.domain.Socialing;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Schema
@Getter
public class SocialingDTO {

    @Schema(description = "해당 모임에 참가한 사용자 (작성자 포함)")
    private Long userId;

    public SocialingDTO(Socialing socialing){
        this.userId = socialing.getUser().getId();
    }
}
