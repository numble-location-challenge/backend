package com.example.backend.dto;

import com.example.backend.domain.User;
import com.example.backend.domain.post.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema
@Getter
@Builder
public class LikesDTO {

    @Schema(description = "좋아요 아이디")
    private Long id;

    @Schema(description = "좋아요 누른 회원 아이디")
    private User user; //좋아요 누른 회원

    @Schema(description = "좋아요 누른 게시글 아이디")
    private Post post; //좋아요 단 글

}
