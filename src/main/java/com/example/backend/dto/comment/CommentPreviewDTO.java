package com.example.backend.dto.comment;

import java.time.LocalDateTime;
import java.util.List;

import com.example.backend.domain.Comment;
import com.example.backend.dto.feed.FeedUserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CommentPreviewDTO {

    @Schema(description = "댓글의 아이디")
    private Long commentId;

    @Schema(description = "댓글을 작성한 유저의 정보")
    private FeedUserDTO user;

    @Schema(description = "댓글의 본문", defaultValue = "댓글 본문입니다.")
    private String contents;

    @Schema(description = "댓글의 생성 시간", defaultValue = "2022-10-22T17:13:39.566884")
    private LocalDateTime createDate;

    private CommentPreviewDTO(Comment comment) {
        this.commentId = comment.getId();
        this.user = FeedUserDTO.toUserDTO(comment.getUser());
        this.contents = comment.getContents();
        this.createDate = comment.getCreateDate();
    }

    public static CommentPreviewDTO findCommentPreview(List<Comment> comments){
        for (int i = comments.size()-1; i >= 0; i--) {
            if (!comments.get(i).getDeleted() && comments.get(i).getParentNum() == 0L) {
                return new CommentPreviewDTO(comments.get(i));
            }
        }
        return null;
    }
}
