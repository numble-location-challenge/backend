package com.example.backend.domain.post;

import com.example.backend.domain.Comment;
import com.example.backend.domain.PostImage;
import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;
import com.example.backend.domain.enumType.SocialStatus;
import com.example.backend.domain.tag.Category;
import com.example.backend.domain.tag.SocialTag;

import com.example.backend.domain.tag.Tag;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@Table(name = "SOCIAL")
@DiscriminatorValue("S")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Social extends Post {

    @OneToMany(mappedBy = "social")
    private List<Socialing> socialings = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "social")
    private List<SocialTag> socialTags = new ArrayList<>();

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private SocialStatus status;

    @NotNull
    private String title; //모임 제목
    @NotNull
    private Integer hits = 0; //조회수

    @NotNull
    @Column(name = "start_date")
    private LocalDateTime startDate; //모임 모집 시작 날짜
    @NotNull
    @Column(name = "end_date")
    private LocalDateTime endDate; //모임 모집 마감 날짜

    @NotNull
    @Column(name = "current_nums")
    private Integer currentNums; //현재 신청한 인원수
    @NotNull
    @Column(name = "limited_nums")
    private Integer limitedNums; //최대 모집 인원수

    @Column(name = "contact")
    private String contact; //연락 방법

    //==수정 메서드==//
    public void updateTitle(String title){
        this.title = title;
    }

    public void updateContents(String contents){
        this.contents = contents;
    }

    public void updateStartDate(LocalDateTime startDate){
        this.startDate = startDate;
    }

    public void updateEndDate(LocalDateTime endDate){
        this.endDate = endDate;
    }

    public void updateLimitedNums(Integer limitedNums){
        this.limitedNums = limitedNums;
    }

    public void updateContact(String contact){
        this.contact = contact;
    }

    //==연관관계 메서드==// user, region은 post 메서드 사용

    public void setSocialTags(List<SocialTag> socialTags){
        this.socialTags = socialTags;
    }

    public void setCategory(Category category){
        this.category = category;
    }

    public void setImages(List<PostImage> images){
        this.images = images;
    }

    //==소셜 생성 메서드==/
    @Builder
    public static Social createSocial(
            User user, int region, String title, String contents,
            LocalDateTime startDate, LocalDateTime endDate, Integer limitedNums, String contact,
            Category category, List<Tag> tags, List<String> image){
        Social social = Social.builder()
                .region(region).title(title).contents(contents)
                .startDate(startDate).endDate(endDate).limitedNums(limitedNums).contact(contact)
                .build();

        social.setUserAndRegion(user); //user의 region을 부모클래스 필드에 저장
        social.setCategory(category);
        social.status=SocialStatus.AVAILABLE;
        social.currentNums=0; //Socialing 생성 시 따로 설정

        //Tag 3개 -> SocialTag 생성 및 세팅
        List<SocialTag> socialTags = tags.stream()
                .map(tag -> SocialTag.createSocialTag(social, tag))
                .collect(toList());
        social.setSocialTags(socialTags);

        //이미지 경로 String 1~3개 -> PostImage 생성 및 세팅
        List<PostImage> postImages = image.stream()
                .map(imagePath -> new PostImage(imagePath, social))
                .collect(toList());
        social.setImages(postImages);

        return social;
    }

    //==비즈니스 로직==//
    //소셜링 참여 시 사용
    public void addCurrentNums(){
        if(currentNums < limitedNums) currentNums++;
    }

    //소셜링 취소, 강퇴 시 사용
    public void minusCurrentNums(){
        if(currentNums > 0) currentNums--;
    }

    public void changeStatusToFull(){
        status = SocialStatus.FULL;
    }

    @Builder
    public Social(User user, List<PostImage> images, List<Comment> comments, String contents, Integer region, int likes,
        Category category, List<SocialTag> socialTags, List<Socialing> socialings, SocialStatus status, String title, Integer hits, LocalDateTime startDate, LocalDateTime endDate, Integer currentNums, Integer limitedNums, String contact) {
        super(user, images, comments, contents, region, likes);
        this.category = category;
        this.socialTags = socialTags;
        this.socialings = socialings;
        this.status = status;
        this.title = title;
        this.hits = hits;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentNums = currentNums;
        this.limitedNums = limitedNums;
        this.contact = contact;
    }
}
