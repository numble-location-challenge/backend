package com.example.backend.dto.social;

import com.example.backend.domain.Socialing;
import lombok.Data;

@Data
public class SocialingDTO {
    private Long socialingId;
    private Long socialId;
    private Long userId;

    public SocialingDTO(Socialing socialing){
        this.socialingId = socialing.getId();
//        this.socialId = socialing.getSocial();
//        this.userId = socialing.getUser();
    }
}
