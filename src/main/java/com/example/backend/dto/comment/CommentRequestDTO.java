package com.example.backend.dto.comment;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "댓글 입력 DTO")
public class CommentRequestDTO {

    @Schema(description = "대댓글의 부모 아이디(대댓글이 아닌 댓글일 시 NULL)", nullable = true, defaultValue = "1")
    private Long commentId;

    @Schema(description = "댓글 본문", defaultValue = "댓글 본문입니다.")
    @NotBlank(message = "본문은 NULL, 공백일 수 없습니다.")
    private String contents;
}
