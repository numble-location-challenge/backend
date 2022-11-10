package com.example.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.backend.domain.PostImage;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Feed;
import com.example.backend.domain.post.Social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
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
    private FeedListSocialDTO social;
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
        this.user = toUserDTO(feed.getUser());
        this.contents = feed.getContents();
        this.thumbnail = getThumbnail(feed.getImages());
        this.social = toFeedListSocialDTO(feed.getSocial());
        this.regions = feed.getRegion();
        //TODO 좋아요 여부 로직 구현 및 시간 로직 구현
        this.isLiked = false;
        this.createTime = feed.getCreateDate();
        this.likes = feed.getLikes();
    }

    public FeedUserDTO toUserDTO(User user){
        return FeedUserDTO.builder()
            .profile(user.getProfile())
            .id(user.getId())
            .nickname(user.getNickname())
            .build();
    }

    public PostImageDTO getThumbnail(List<PostImage> images){
        if (images.isEmpty()){
            return null;
        }
        return new PostImageDTO(images.get(0));
    }

    @Schema(description = "피드와 연결된 모임글 DTO")
    @Getter
    @NoArgsConstructor
    static class FeedListSocialDTO {
        @Schema(description = "피드와 연결된 모임글의 아이디", defaultValue = "15")
        private Long id;
        @Schema(description = "피드와 연결된 모임글의 제목", defaultValue = "모임글 제목")
        private String title;
        @Schema(description = "피드와 연결된 모임글의 지역", defaultValue = "30")
        private Integer region;
        @Schema(description = "피드와 연결된 모임글의 모임 시작 시간",defaultValue = "YYYY-MM-DDT20:30")
        private LocalDateTime startDate;

        @Builder
        public FeedListSocialDTO(Long id, String title, Integer region, LocalDateTime startDate) {
            this.id = id;
            this.title = title;
            this.region = region;
            this.startDate = startDate;
        }
    }

    private FeedListSocialDTO toFeedListSocialDTO(Social social){
        return FeedListSocialDTO.builder()
            .id(social.getId())
            .region(social.getRegion())
            .startDate(social.getStartDate())
            .title(social.getTitle())
            .build();
    }
}
