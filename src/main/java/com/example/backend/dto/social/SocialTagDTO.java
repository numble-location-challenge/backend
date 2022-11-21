package com.example.backend.dto.social;

import com.example.backend.domain.tag.SocialTag;
import com.example.backend.domain.tag.Tag;
import com.example.backend.dto.TagDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema
public class SocialTagDTO {
    private Long tag_id;
    private String tag_name;

    public SocialTagDTO(SocialTag socialTag){
        this.tag_id = socialTag.getTag().getId();
        this.tag_name = socialTag.getTag().getName();
    }
}
