package com.example.backend.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "피드 작성 DTO")
@Builder
@Data
public class FeedRequestDTO {

    @Schema(description = "본문")
    private String contents;
    @Schema(description = "피드에 연결하는 소셜의 아이디", example = "아이디", nullable = true)
    private Long socialId;
    @Schema(description = "피드에 보여주고 싶은 이미지의 이름", example = "1c0a2b10-30d4-6aaf.jpg")
    private List<PostImageDTO> images;
    @Schema(description = "피드의 지역")
    private Integer region;

}
