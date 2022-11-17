package com.example.backend.dto.social;

import com.example.backend.domain.enumType.SocialStatus;
import com.example.backend.domain.post.Social;
import com.example.backend.domain.tag.Category;
import com.example.backend.dto.CategoryDTO;
import com.example.backend.dto.LikesDTO;
import com.example.backend.dto.PostImageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "모임 목록에 출력할 데이터 DTO (일부 데이터만 출력)")
public class SocialShortDTO {

    @Schema(description = "모임에 참여한 사용자")
    private List<SocialingDTO> socialings = new ArrayList<>();

    @Schema(description = "모임 게시글 아이디")
    @NotNull
    private Long id;

    @Schema(description = "모임 게시글에 첨부한 사진 (1장)")
    private List<PostImageDTO> images;

    @Schema(description = "작성자의 지역")
    private int region;

    @Schema(description = "모임 게시글 제목")
    @NotNull
    private String title; //모임 제목

    @Schema(description = "모임 종료 날짜")
    @NotNull
    private LocalDateTime endDate; //모임 끝나는 날짜

    @Schema(description = "현재 모임에 신청한 인원")
    private int currentNums; //현재 신청한 인원수

    @Schema(description = "최대 모집 인원")
    @NotNull
    private int limitedNums; //최대 모집 인원수

    @Schema(description = "모임 신청 가능 유무")
    @NotNull
    private SocialStatus status; //신청 가능 유무

    @Schema(description = "대분류")
    private CategoryDTO category;

    @Schema(description = "소분류")
    private List<SocialTagDTO> tags;

    @Schema(name = "좋아요 개수", description = "인기순 정렬에 사용")
    private int likeCnt;

    @Schema(name = "게시글 생성 시간", description = "최신순 정렬에 사용")
    private LocalDateTime createDate;

    public SocialShortDTO(Social social) {
        this.socialings = social.getSocialings().stream().map(socialing -> new SocialingDTO(socialing)).collect(Collectors.toList());
        this.id = social.getId();
        this.images = social.getImages().stream().map(postImage -> new PostImageDTO(postImage)).collect(Collectors.toList());
        this.region = social.getRegion();
        this.title = social.getTitle();
        this.endDate = social.getEndDate();
        this.currentNums = social.getCurrentNums();
        this.limitedNums = social.getLimitedNums();
        this.status = social.getStatus();
        this.category = toCategoryDTO(social.getCategory());
        this.tags = social.getSocialTags().stream().map(socialTag -> new SocialTagDTO(socialTag)).collect(Collectors.toList());
        this.likeCnt = social.getLikes();
        this.createDate = social.getCreateDate();
    }

    public CategoryDTO toCategoryDTO(Category category){
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
