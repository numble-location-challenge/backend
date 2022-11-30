package com.example.backend.dto;

import com.example.backend.domain.Like;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema
@Getter
public class LikesDTO {

    @Schema(description = "좋아요 아이디")
    private Long id;

    @Schema(description = "좋아요 누른 회원 아이디")
    private Long user; //좋아요 누른 회원

    @Schema(description = "좋아요 누른 게시글 아이디")
    private Long post; //좋아요 단 글

    @Schema(description = "좋아요 선택/해제 여부")
    private boolean orElse = false;

    @Builder
    public LikesDTO(Like like){
        this.id = like.getId();
        this.user = like.getUser().getId();
        this.post = like.getPost().getId();
    }

    public void updateOrElse(boolean orElse){
        this.orElse = orElse;
    }

}
