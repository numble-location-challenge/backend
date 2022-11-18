package com.example.backend.dto.feed;

import static com.example.backend.dto.feed.FeedUserDTO.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.backend.domain.post.Feed;
import com.example.backend.dto.CommentResponseDTO;
import com.example.backend.dto.PostImageDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "피드 단건 조회 DTO")
@Data
@NoArgsConstructor
public class FeedResponseDTO {
    @Schema(description = "피드의 아이디", defaultValue = "67")
    private Long id;
    @Schema(description = "피드를 작성한 유저")
    private FeedUserDTO user;
    @Schema(description = "피드 본문", defaultValue = "본문입니다.")
    private String contents;
    @Schema(description = "피드와 연결된 소셜")
    private FeedSocialDTO social;
    @Schema(description = "피드의 이미지")
    private List<PostImageDTO> images = new ArrayList<>();
    @Schema(description = "피드의 댓글")
    private List<CommentResponseDTO> comments = new ArrayList<>();
    @Schema(description = "피드의 지역",defaultValue = "3")
    private Integer regions;
    @Schema(description = "좋아요 체크 여부", defaultValue = "false")
    private boolean isLiked;
    @Schema(description = "피드의 생성 날짜 및 시간", defaultValue = "YYYY-MM-DDT20:30")
    private LocalDateTime createTime;
    @Schema(description = "피드의 좋아요 수", defaultValue = "6")
    private int likes;

    public FeedResponseDTO(Feed feed) {
        this.id = feed.getId();
        this.user = toUserDTO(feed.getUser());
        this.contents = feed.getContents();
        if (feed.getSocial() != null) {
            this.social = FeedSocialDTO.toFeedSocialDTO(feed.getSocial());
        }
        this.images = feed.getImages().stream().map(postImage -> new PostImageDTO(postImage)).collect(Collectors.toList());
        this.comments = feed.getComments().stream().map(comment -> new CommentResponseDTO(comment)).collect(Collectors.toList());
        this.regions = feed.getRegion();
        //TODO 좋아요 여부 로직 구현 및 시간 로직 구현
        this.isLiked = false;
        this.createTime = feed.getCreateDate();
        this.likes = feed.getLikes();
    }
}
