package com.example.backend.dto.feed;

import java.time.LocalDateTime;

import com.example.backend.domain.post.Feed;
import com.example.backend.dto.PostImageDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "피드 리스트 출력")
@Data
@NoArgsConstructor
public class FeedListResponseDTO {

    @Schema(description = "피드의 아이디", defaultValue = "10")
    private Long id;
    @Schema(description = "피드 작성자 DTO")
    private FeedUserDTO user;
    @Schema(description = "피드 본문", defaultValue = "본문입니다.")
    private String contents;
    @Schema(description = "피드와 연결된 소셜 DTO")
    private FeedSocialDTO social;
    @Schema(description = "피드 썸네일")
    private PostImageDTO thumbnail;
    @Schema(description = "피드 지역", defaultValue = "2")
    private Integer regions;
    @Schema(description = "피드 좋아요 여부",defaultValue = "false")
    private boolean isLiked;
    @Schema(description = "피드 생성시간", defaultValue = "YYYY-MM-DDT20:30")
    private LocalDateTime createTime;
    @Schema(description = "피드 좋아요 수", defaultValue = "3")
    private int likes;

    public FeedListResponseDTO(Feed feed) {
        this.id = feed.getId();
        this.user = FeedUserDTO.toUserDTO(feed.getUser());
        this.contents = feed.getContents();
        // this.thumbnail = PostImageDTO.getThumbnail(feed.getImages());
        this.social = FeedSocialDTO.toFeedSocialDTO(feed.getSocial());
        this.regions = feed.getRegionCode();
        //TODO 좋아요 여부 로직 구현 및 시간 로직 구현
        this.isLiked = false;
        this.createTime = feed.getCreateDate();
        this.likes = feed.getLikes();
    }
}
