package com.example.backend.dto.feed;

import java.time.LocalDateTime;

import com.example.backend.domain.post.Social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "피드와 연결된 모임글 DTO")
@Getter
@NoArgsConstructor
public class FeedSocialDTO {
    @Schema(description = "피드와 연결된 모임글의 아이디", defaultValue = "15")
    private Long id;
    @Schema(description = "피드와 연결된 모임글의 제목", defaultValue = "모임글 제목")
    private String title;
    @Schema(description = "피드와 연결된 모임글의 지역", defaultValue = "30")
    private Integer region;
    @Schema(description = "피드와 연결된 모임글의 모임 시작 시간",defaultValue = "YYYY-MM-DDT20:30")
    private LocalDateTime startDate;

    @Builder
    public FeedSocialDTO(Long id, String title, Integer region, LocalDateTime startDate) {
        this.id = id;
        this.title = title;
        this.region = region;
        this.startDate = startDate;
    }

    public static FeedSocialDTO toFeedSocialDTO(Social social){
        return FeedSocialDTO.builder()
            .id(social.getId())
            .region(social.getRegion())
            .startDate(social.getStartDate())
            .title(social.getTitle())
            .build();
    }
}