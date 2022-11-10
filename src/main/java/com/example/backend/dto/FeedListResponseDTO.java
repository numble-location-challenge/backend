package com.example.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.backend.domain.PostImage;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Feed;
import com.example.backend.domain.post.Social;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeedListResponseDTO {

    private Long id;
    private UserDto user;
    private String contents;
    private FeedListSocialDTO social;
    private PostImageDTO thumbnail;
    private Integer regions;
    private boolean isLiked;
    private LocalDateTime createTime;
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

    public UserDto toUserDTO(User user){
        return UserDto.builder()
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
    @Getter
    @NoArgsConstructor
    static class FeedListSocialDTO {
        private Long id;
        private String title;
        private Integer region;
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
