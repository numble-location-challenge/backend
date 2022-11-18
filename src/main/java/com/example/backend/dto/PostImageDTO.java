package com.example.backend.dto;

import java.util.List;

import com.example.backend.domain.PostImage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이미지 저장 경로")
@Getter
@NoArgsConstructor
public class PostImageDTO {

    @Schema(description = "이미지 경로", defaultValue = "1c0a2b10-30d4-6aaf.jpg")
    private String imagePath;

    public PostImageDTO(PostImage postImage) {
        this.imagePath = postImage.getImagePath();
    }

    public static PostImageDTO getThumbnail(List<PostImage> images){
        if (images.isEmpty()){
            return null;
        }
        return new PostImageDTO(images.get(0));
    }
}
