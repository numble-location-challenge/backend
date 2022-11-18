package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema
public class TagDTO {
    @Schema(description = "태그", defaultValue = "등산")
    private String tag;
}
