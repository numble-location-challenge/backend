package com.example.backend.dto.social;

import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;
import com.example.backend.domain.enumType.SocialStatus;
import com.example.backend.domain.post.Social;
import com.example.backend.domain.tag.Category;
import com.example.backend.domain.tag.SocialTag;
import com.example.backend.dto.*;
import com.example.backend.dto.comment.CommentResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "모임 단건 조회에 출력할 데이터 DTO (모든 데이터 출력)")
public class SocialLongDTO{

    @Schema(description = "모임 게시글 아이디", defaultValue = "12")
    @NotNull
    private Long id;

    @Schema(description =  "모임 게시글 작성한 사용자")
    @NotNull
    private SocialUserDTO user;

    @Schema(description = "모임 게시글 제목",defaultValue = "모임 제목")
    @NotNull
    private String title; //모임 제목

    @Schema(description = "모임 게시글 내용", defaultValue = "바로 같이, 가슴이 어디 하는 갑 우는 칼이다.")
    private String contents;

    @Schema(description = "모임 시작 날짜")
    @NotNull
    private LocalDateTime startDate; //모임 시작날짜

    @Schema(description = "모임 종료 날짜")
    @NotNull
    private LocalDateTime endDate; //모임 끝나는 날짜

    @Schema(description = "모임 게시글에 첨부한 사진")
    private List<PostImageDTO> images = new ArrayList<>();

    @Schema(description = "모임 게시글에 달린 댓글")
    private List<CommentResponseDTO> comments = new ArrayList<>();

    @Schema(description = "작성자의 행정구역 시군구코드 5자리", example = "11010")
    private int regionCode;

    @Schema(description = "작성자의 행정구역 동읍면코드 8자리", example = "1101053")
    private Long dongCode;

    @Schema(description = "행정구역명", example = "서울특별시 종로구 사직동")
    private String dongName;

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

    @Schema(description = "좋아요 개수")
    private int likesCnt;

    @Schema(description = "좋아요 여부")
    private boolean likeOrElse;

    @Schema(description = "모임에 참여한 사용자")
    private List<SocialingDTO> socialings = new ArrayList<>();

    public SocialLongDTO(Social social) {
        this.socialings = toSocialingDTO(
                social.getSocialings().stream()
                        .filter(socialing -> socialing.getSocial().getId().equals(social.getId()))
                        .collect(Collectors.toList())
        );
        this.id = social.getId();
        this.user = toSocialUserDTO(social.getUser());
        this.images = social.getImages().stream().map(PostImageDTO::new).collect(Collectors.toList());
        this.comments = social.getComments().stream().map(CommentResponseDTO::new).collect(Collectors.toList());
        this.contents = social.getContents();
        this.regionCode = social.getRegionCode();
        this.dongCode = social.getDongCode();
        this.dongName = social.getDongName();
        this.title = social.getTitle();
        this.startDate = social.getStartDate();
        this.endDate = social.getEndDate();
        this.currentNums = social.getCurrentNums();
        this.limitedNums = social.getLimitedNums();
        this.status = social.getStatus();
        this.contact = social.getContact();
        this.category = toCategoryDTO(social.getCategory());
        this.tags = toSocialTagDTO(
            social.getSocialTags()
                .stream().filter(socialTag -> socialTag.getSocial().getId().equals(social.getId()))
                .collect(Collectors.toList())
        );
        this.likesCnt = social.getLikes();
    }

    public void updateLikeOrElse(boolean orElse){
        this.likeOrElse = orElse;
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

    public List<SocialTagDTO> toSocialTagDTO(List<SocialTag> socialTags){
        return socialTags.stream()
                .map(SocialTagDTO::new)
                .collect(Collectors.toList());
    }

    public List<SocialingDTO> toSocialingDTO(List<Socialing> socialings){
        return socialings.stream().map(SocialingDTO::new).collect(Collectors.toList());
    }
}
