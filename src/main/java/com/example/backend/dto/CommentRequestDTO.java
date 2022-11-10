package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 입력 DTO")
public class CommentRequestDTO {

    @Schema(description = "댓글 본문", defaultValue = "댓글 본문입니다.")
    private String contents;
}
