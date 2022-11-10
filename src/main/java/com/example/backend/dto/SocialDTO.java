package com.example.backend.dto;

import com.example.backend.domain.Comment;
import com.example.backend.domain.PostImage;
import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;
import com.example.backend.domain.enumType.SocialStatus;
import com.example.backend.domain.post.Social;
import com.example.backend.domain.tag.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema
public class SocialDTO {

    @Schema(description = "모임에 참여한 사용자")
    private List<Socialing> socialings = new ArrayList<>(); //?

    @Schema(description = "모임 게시글 아이디")
    @NotNull
    private Long id;

    @Schema(description =  "모임 게시글 작성한 사용자")
    @NotNull
    private User user;

    @Schema(description = "모임 게시글에 첨부한 사진")
    private List<PostImage> images;

    @Schema(description = "모임 게시글에 달린 댓글")
    private List<Comment> comments;

    @Schema(description = "게시글 카테고리")
    private List<PostCategory> category;

    @Schema(description = "모임 게시글 내용")
    private String contents;

    @Schema(description = "작성자의 지역")
    private int region;

    @Schema(description = "해당 게시글 좋아요 수")
    private int likes;

    @Schema(description = "모임 게시글 제목")
    @NotNull
    private String title; //모임 제목

    @Schema(description = "조회수")
    private int hits; //조회수

    @Schema(description = "모임 시작 날짜")
    @NotNull
    private LocalDateTime startDate; //모임 시작날짜

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

    @Schema(description = "모임 주최자 연락 방법")
    private String contact; //연락 방법

    private Social toEntity(){
        return Social.builder()
                .user(user).images(images).comments(comments)
                .category(category).contents(contents).region(region).likes(likes)
                .title(title).hits(hits)
                .startDate(startDate).endDate(endDate)
                .currentNums(currentNums).limitedNums(limitedNums)
                .status(status).contact(contact)
                .build();
    }


}
