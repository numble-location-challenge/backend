package com.example.backend.dto.feed;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "피드 리스트 페이징 처리를 위한 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedPagingDTO<T> {
    private boolean success;
    private String message;
    private T data;
    @Schema(description = "다음 페이지가 존재하는지 여부")
    boolean hasNextPage;

    public FeedPagingDTO(T data,String message, boolean hasNextPage) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.hasNextPage = hasNextPage;
    }
}
