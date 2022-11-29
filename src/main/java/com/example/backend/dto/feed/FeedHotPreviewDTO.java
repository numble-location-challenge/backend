package com.example.backend.dto.feed;

import com.example.backend.domain.post.Feed;
import com.example.backend.dto.PostImageDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class FeedHotPreviewDTO {

    @Schema(description = "피드의 아이디", defaultValue = "10")
    private Long postId;
    @Schema(description = "피드 본문", defaultValue = "본문입니다.")
    private String contents;
    @Schema(description = "피드 좋아요 여부",defaultValue = "false")
    private boolean isLiked;
    @Schema(description = "피드 썸네일")
    private PostImageDTO thumbnail;
    @Schema(description = "피드 좋아요 수", defaultValue = "3")
    private int likes;

    public FeedHotPreviewDTO(Feed feed) {
        this.postId = feed.getId();
        this.contents = feed.getContents();
        this.isLiked = feed.isLiked();
        this.thumbnail = PostImageDTO.getThumbnail(feed.getImages());
        this.likes = feed.getLikes();
    }
}
