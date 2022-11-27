package com.example.backend.dto.feed;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "피드 검색 및 페이징")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedSearch {

    private static final int MAX_PAGE_SIZE = 20;

    @Schema(description = "조회할 피드 필터링 (LATEST, HOT)", defaultValue = "LATEST")
    @Builder.Default
    private String filter = "LATEST";
    @Schema(description = "페이지 번호 (1부터 시작)", defaultValue = "1")
    @Builder.Default
    private Integer page = 1;
    @Schema(description = "페이지 당 최대 게시글 수", defaultValue = "20")
    @Builder.Default
    private Integer size = 20;

}
