package com.example.backend.dto.comment;

import com.example.backend.domain.Comment;
import com.example.backend.dto.feed.FeedUserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "댓글 조회 DTO")
@Getter
@NoArgsConstructor
public class CommentResponseDTO {
    @Schema(description = "댓글의 유저 정보")
    private FeedUserDTO user;
    @Schema(description = "댓글의 본문", defaultValue = "댓글 본문입니다.")
    private String contents;
    @Schema(description = "댓글의 그룹",defaultValue = "1")
    private int cGroup;
    @Schema(description = "댓글 그룹 내에서의 레벨 (댓글 : 0 / 대댓글 : 1", defaultValue = "1")
    private int level;
    @Schema(description = "댓글 그룹 내에서의 정렬 순서", defaultValue = "2")
    private int refOrder;
    @Schema(description = "대댓글 부모의 댓글 아이디",defaultValue = "3")
    private Long parentNum;

    public CommentResponseDTO(Comment comment) {
        this.user = FeedUserDTO.toUserDTO(comment.getUser());
        this.contents = comment.getContents();
        this.cGroup = comment.getCGroup();
        this.level = comment.getLevel();
        this.refOrder = comment.getRefOrder();
        this.parentNum = comment.getParentNum();
    }
}
