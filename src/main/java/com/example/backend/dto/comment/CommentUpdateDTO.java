package com.example.backend.dto.comment;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "댓글 수정 DTO")
@Getter
@Setter
public class CommentUpdateDTO {
    @Schema(description = "수정할 댓글의 내용", defaultValue = "수정할 댓글내용입니다.")
    @NotBlank
    private String contents;
}
