package com.example.backend.dto.feed;

import java.time.LocalDateTime;

import com.example.backend.domain.post.Social;
import com.example.backend.dto.PostImageDTO;

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
    @Schema(description = "피드와 연결된 모임글의 이미지 경로", defaultValue = "353d-daf3.jpg")
    private PostImageDTO thumbnail;
    @Schema(description = "피드와 연결된 모임글의 제목", defaultValue = "모임글 제목")
    private String title;
    @Schema(description = "피드와 연결된 모임글의 지역", defaultValue = "30")
    private Integer region;
    @Schema(description = "피드의 지역 이름", defaultValue = "서울특별시 종로구 효자동")
    private String regionName;
    @Schema(description = "피드와 연결된 모임글의 모임 시작 시간",defaultValue = "YYYY-MM-DDT20:30")
    private LocalDateTime startDate;

    @Builder
    public FeedSocialDTO(Long id, PostImageDTO thumbnail, String title, Integer region, String regionName,
        LocalDateTime startDate) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.region = region;
        this.regionName = regionName;
        this.startDate = startDate;
    }

    public static FeedSocialDTO toFeedSocialDTO(Social social){
        return FeedSocialDTO.builder()
            .id(social.getId())
            .thumbnail(PostImageDTO.getThumbnail(social.getImages()))
            .region(social.getRegionCode())
            .regionName(social.getDongName())
            .startDate(social.getStartDate())
            .title(social.getTitle())
            .build();
    }
}
