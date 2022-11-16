package com.example.backend.dto.social;

import com.example.backend.domain.tag.SocialTag;
import com.example.backend.dto.TagDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema
public class SocialTagDTO {
    private Long id;
    private Long social;
    private Long tag;

    public SocialTagDTO(SocialTag socialTag){
        this.id = socialTag.getId();
        this.social = socialTag.getId();
        this.tag = socialTag.getId();
    }
}
