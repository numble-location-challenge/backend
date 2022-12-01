package com.example.backend.dto.comment;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "댓글 입력, 수정 DTO")
public class CommentRequestDTO {

    @Schema(description = "댓글 본문", defaultValue = "댓글 본문입니다.")
    @NotBlank(message = "본문은 NULL, 공백일 수 없습니다.")
    private String contents;
}
