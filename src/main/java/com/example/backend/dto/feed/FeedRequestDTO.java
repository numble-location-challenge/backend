package com.example.backend.dto.feed;

import java.util.List;

import com.example.backend.dto.PostImageDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "피드 작성 DTO")
@Builder
@Data
public class FeedRequestDTO {

    @Schema(description = "피드 본문", defaultValue = "본문입니다.")
    private String contents;
    @Schema(description = "피드에 연결하는 소셜의 아이디", defaultValue = "3", nullable = true)
    private Long socialId;
    @Schema(description = "피드의 이미지 리스트")
    private List<PostImageDTO> images;
    @Schema(description = "피드의 지역", defaultValue = "3")
    private Integer region;
}
