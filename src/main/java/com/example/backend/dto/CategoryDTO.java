package com.example.backend.dto;

import com.example.backend.domain.post.Social;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema
@Getter
@Builder
public class CategoryDTO {
    @Schema(description = "카테고리 아이디")
    private Long id;
    @Schema(description = "카테고리 명")
    private String name;
}
