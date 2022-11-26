package com.example.backend.dto.comment;

import java.time.LocalDateTime;

import com.example.backend.domain.Comment;
import com.example.backend.dto.feed.FeedUserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "댓글 조회 DTO 삭제된 댓글일 시 deleted= true, user = null, content = 삭제된 댓글입니다.")
@Getter
@NoArgsConstructor
public class CommentResponseDTO {
    @Schema(description = "댓글의 아이디")
    private Long commentId;
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
    @Schema(description = "댓글의 생성 시간", defaultValue = "2022-10-22T17:13:39.566884")
    private LocalDateTime createDate;
    @Schema(description = "해당 댓글의 삭제 여부", defaultValue = "false")
    private Boolean deleted;


    public CommentResponseDTO(Comment comment) {
        this.commentId = comment.getId();
        this.cGroup = comment.getCGroup();
        this.level = comment.getLevel();
        this.refOrder = comment.getRefOrder();
        this.parentNum = comment.getParentNum();
        if (comment.getDeleted()) {
            this.contents = "삭제된 댓글입니다.";
        } else {
            this.contents = comment.getContents();
            this.user = FeedUserDTO.toUserDTO(comment.getUser());
            this.createDate = comment.getCreateDate();
        }
        deleted = comment.getDeleted();
    }
}
