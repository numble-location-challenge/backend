package com.example.backend.dto.feed;

import com.example.backend.domain.post.Feed;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "내가 작성한 피드 ResponseDTO")
@Getter
public class MyFeedResponseDTO {

    @Schema(description = "피드 아이디")
    private Long postId;
    @Schema(description = "피드 썸네일")
    private String imagePath;

    public MyFeedResponseDTO(Feed feed) {
        this.postId = feed.getId();
        this.imagePath = feed.getImages().get(0).getImagePath();
    }
}
