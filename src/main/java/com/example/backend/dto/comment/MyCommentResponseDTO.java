package com.example.backend.dto.comment;

import java.time.LocalDateTime;

import com.example.backend.domain.Comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "내가 쓴 댓글 조회 DTO")
@Getter
public class MyCommentResponseDTO {
    @Schema(description = "댓글이 속한 게시물의 아이디")
    private Long postId;

    @Schema(description = "댓글의 아이디")
    private Long commentId;

    @Schema(description = "댓글의 본문", defaultValue = "댓글 본문입니다.")
    private String contents;

    @Schema(description = "댓글의 생성 시간", defaultValue = "2022-10-22T17:13:39.566884")
    private LocalDateTime createDate;

    public MyCommentResponseDTO(Comment comment) {
        this.postId = comment.getPost().getId();
        this.commentId = comment.getId();
        this.contents = comment.getContents();
        this.createDate = comment.getCreateDate();
    }
}
