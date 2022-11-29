package com.example.backend.dto.comment;

import java.time.LocalDateTime;

import com.example.backend.domain.Comment;
import com.example.backend.dto.feed.FeedUserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentPreviewDTO {

    @Schema(description = "댓글의 아이디")
    private Long commentId;

    @Schema(description = "댓글을 작성한 유저의 정보")
    private FeedUserDTO user;

    @Schema(description = "댓글의 본문", defaultValue = "댓글 본문입니다.")
    private String contents;

    @Schema(description = "댓글의 생성 시간", defaultValue = "2022-10-22T17:13:39.566884")
    private LocalDateTime createDate;

    public static CommentPreviewDTO createCommentPreviewDTO(Comment comment) {
        return CommentPreviewDTO.builder()
            .commentId(comment.getId())
            .user(FeedUserDTO.toUserDTO(comment.getUser()))
            .contents(comment.getContents())
            .createDate(comment.getCreateDate())
            .build();
    }
}
