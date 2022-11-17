package com.example.backend.dto;

import com.example.backend.domain.tag.Category;
import com.example.backend.domain.tag.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Schema
@Getter
@Builder
public class TagDTO {
    @Schema(description = "소분류 아이디")
    private Long id;

    @Schema(description = "태그 명")
    private String name;

    @Schema(description = "태그와 연결된 대분류")
    private Long category;

    public TagDTO(Tag tag){
        this.id = tag.getId();
        this.name = tag.getName();
        this.category = tag.getCategory().getId();
    }


}
