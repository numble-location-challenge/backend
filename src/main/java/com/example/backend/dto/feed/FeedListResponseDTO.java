package com.example.backend.dto.feed;

import java.time.LocalDateTime;
import java.util.List;

import com.example.backend.domain.Comment;
import com.example.backend.domain.post.Feed;
import com.example.backend.dto.PostImageDTO;
import com.example.backend.dto.comment.CommentPreviewDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "피드 리스트 출력")
@Data
@NoArgsConstructor
public class FeedListResponseDTO {

    @Schema(description = "피드의 아이디", defaultValue = "10")
    private Long postId;
    @Schema(description = "피드 작성자 DTO")
    private FeedUserDTO user;
    @Schema(description = "피드 본문", defaultValue = "본문입니다.")
    private String contents;
    @Schema(description = "피드 썸네일")
    private PostImageDTO thumbnail;
    @Schema(description = "피드의 지역 코드", defaultValue = "1111")
    private Integer regions;
    @Schema(description = "피드의 지역 이름", defaultValue = "서울특별시 종로구 효자동")
    private String regionName;
    @Schema(description = "피드 생성시간", defaultValue = "YYYY-MM-DDT20:30")
    private LocalDateTime createTime;
    @Schema(description = "피드 좋아요 수", defaultValue = "3")
    private int likes;
    @Schema(description = "피드 좋아요 여부",defaultValue = "false")
    private boolean isLiked;
    @Schema(description = "해당 피드의 총 댓글 갯수", defaultValue = "10")
    private int comments_cnt;
    @Schema(description = "댓글 리스트에서 가장 최신의 댓글 미리보기 DTO")
    private CommentPreviewDTO comment;
    @Schema(description = "피드와 연결된 소셜 DTO")
    private FeedSocialDTO social;

    public FeedListResponseDTO(Feed feed) {
        this.postId = feed.getId();
        this.user = FeedUserDTO.toUserDTO(feed.getUser());
        this.contents = feed.getContents();
        this.thumbnail = PostImageDTO.getThumbnail(feed.getImages());
        if (feed.getSocial() != null) {
            this.social = FeedSocialDTO.toFeedSocialDTO(feed.getSocial());
        }
        this.comment = findCommentPreview(feed.getComments());
        this.regions = feed.getRegionCode();
        this.regionName = feed.getDongName();
        this.isLiked = feed.isLiked();
        this.createTime = feed.getCreateDate();
        this.likes = feed.getLikes();
        this.comments_cnt = feed.getCommentCount();
    }

    private CommentPreviewDTO findCommentPreview(List<Comment> comments){
            for (int i = comments.size()-1; i >= 0; i--) {
                if (!comments.get(i).getDeleted() && comments.get(i).getParentNum() == 0L) {
                    return CommentPreviewDTO.createCommentPreviewDTO(comments.get(i));
                }
            }
        return null;
    }
}
