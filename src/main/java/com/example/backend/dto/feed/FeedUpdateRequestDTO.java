package com.example.backend.dto.feed;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.backend.dto.PostImageDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "피드 작성 DTO")
@Builder
@Data
public class FeedUpdateRequestDTO {
    @Schema(description = "피드 본문", defaultValue = "본문입니다.")
    @NotBlank
    private String contents;
    @Schema(description = "피드에 연결하는 소셜의 아이디", defaultValue = "3", nullable = true)
    private Long socialId;
    @Schema(description = "피드의 이미지 리스트")
    @Size(min = 1, max = 3)
    private List<PostImageDTO> images;
}
