package com.example.backend.dto.social;

import com.example.backend.domain.tag.SocialTag;
import com.example.backend.dto.TagDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema
public class SocialTagDTO {
    private Long id;
    private SocialShortDTO social;
    private TagDTO tag;

    public SocialTagDTO(SocialTag socialTag){
//        this.id = socialTag.id;
//        this.social = socialTag.getSocial;
//        this.tag = socialTag.getTag;
    }
}
