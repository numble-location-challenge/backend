package com.example.backend.dto.social;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.SocialStatus;
import com.example.backend.domain.post.Social;
import com.example.backend.domain.tag.Category;
import com.example.backend.dto.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Schema(description = "모임 단건 조회에 출력할 데이터 DTO (모든 데이터 출력)")
@Builder
public class SocialLongDTO{

    @Schema(description = "모임에 참여한 사용자")
    private List<SocialingDTO> socialings = new ArrayList<>();

    @Schema(description = "모임 게시글 아이디", defaultValue = "12")
    @NotNull
    private Long id;

    @Schema(description =  "모임 게시글 작성한 사용자")
    @NotNull
    private SocialUserDTO user;

    @Schema(description = "모임 게시글에 첨부한 사진")
    private List<PostImageDTO> images = new ArrayList<>();

    @Schema(description = "모임 게시글에 달린 댓글")
    private List<CommentResponseDTO> comments = new ArrayList<>();

    @Schema(description = "모임 게시글 내용", defaultValue = "바로 같이, 가슴이 어디 하는 갑 우는 칼이다.")
    private String contents;

    @Schema(description = "작성자의 지역", defaultValue = "1")
    private int region;

    @Schema(description = "모임 게시글 제목",defaultValue = "모임 제목")
    @NotNull
    private String title; //모임 제목

    @Schema(description = "모임 시작 날짜")
    @NotNull
    private LocalDateTime startDate; //모임 시작날짜

    @Schema(description = "모임 종료 날짜")
    @NotNull
    private LocalDateTime endDate; //모임 끝나는 날짜

    @Schema(description = "현재 모임에 신청한 인원",defaultValue = "4")
    private int currentNums; //현재 신청한 인원수

    @Schema(description = "최대 모집 인원",defaultValue = "8")
    @NotNull
    private int limitedNums; //최대 모집 인원수

    @Schema(description = "모임 신청 가능 유무",defaultValue = "AVAILABLE")
    @NotNull
    private SocialStatus status; //신청 가능 유무

    @Schema(description = "모임 주최자 연락 방법")
    private String contact; //연락 방법

    @Schema(description = "대분류")
    private CategoryDTO category;

    @Schema(description = "소분류")
    private List<SocialTagDTO> tags;

    public SocialLongDTO(Social social) {
        this.socialings = social.getSocialings().stream().map(socialings -> new SocialingDTO(socialings)).collect(Collectors.toList());
        this.id = social.getId();
        this.user = toSocialUserDTO(social.getUser());
        this.images = social.getImages().stream().map(postImage -> new PostImageDTO(postImage)).collect(Collectors.toList());
        this.comments = social.getComments().stream().map(comment -> new CommentResponseDTO(comment)).collect(Collectors.toList());
        this.contents = social.getContents();
        this.region = social.getRegion();
        this.title = social.getTitle();
        this.startDate = social.getStartDate();
        this.endDate = social.getEndDate();
        this.currentNums = social.getCurrentNums();
        this.limitedNums = social.getLimitedNums();
        this.status = social.getStatus();
        this.contact = social.getContact();
        this.category = toCategoryDTO(social.getCategory());
        this.tags = social.getSocialTags().stream().map(tag -> new SocialTagDTO(tag)).collect(Collectors.toList());
    }

    public CategoryDTO toCategoryDTO(Category category){
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public SocialUserDTO toSocialUserDTO(User user){
        return SocialUserDTO.builder()
                .id(user.getId())
                .name(user.getNickname())
                .build();
    }
}
