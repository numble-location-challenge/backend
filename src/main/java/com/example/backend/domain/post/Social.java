package com.example.backend.domain.post;

import com.example.backend.domain.Comment;
import com.example.backend.domain.PostImage;
import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;
import com.example.backend.domain.enumType.SocialStatus;
import com.example.backend.domain.tag.Category;
import com.example.backend.domain.tag.SocialTag;

import com.example.backend.domain.tag.Tag;
import com.example.backend.dto.PostImageDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @OneToMany(mappedBy = "social", cascade = CascadeType.ALL)
    private List<Socialing> socialings = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "social", cascade = CascadeType.ALL)
    private List<SocialTag> socialTags = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private SocialStatus status;

    @NotNull
    private String title; //모임 제목

    @NotNull
    private Integer hits; //조회수

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
    public void addSocialing(Socialing socialing){
        socialings.add(socialing);
        socialing.setSocial(this);
        addCurrentNums();
    }

    public void setSocialTags(List<SocialTag> socialTags){
        this.socialTags = socialTags;
    }

    public void setCategory(Category category){
        this.category = category;
    }

    public void setImages(List<PostImage> images){
        for(PostImage image : images){
            image.setPost(this);
        }
        this.images = images;
    }

    //==소셜 생성 메서드==//
    public static Social createSocial(
            User user, String title, String contents, String contact,
            LocalDateTime startDate, LocalDateTime endDate, Integer limitedNums,
            List<PostImage> postImages, Category category, List<Tag> tags, Socialing socialing){

        Social social = Social.builder()
                .user(user).title(title).contents(contents).contact(contact)
                .startDate(startDate).endDate(endDate).limitedNums(limitedNums)
                .likes(0).currentNums(0).hits(0) //currentNums는 Socialing 생성 시 따로 설정
                .status(SocialStatus.AVAILABLE)
                .socialings(new ArrayList<>())
                .build();

        social.setCategory(category);
        social.setImages(postImages);
        social.addSocialing(socialing);

        //Tag 3개 -> SocialTag 생성 및 세팅
        List<SocialTag> socialTags = tags.stream()
                .map(tag -> SocialTag.createSocialTag(social, tag))
                .collect(toList());
        social.setSocialTags(socialTags);

        return social;
    }

    //==비즈니스 로직==//
    //소셜링 참여 시 사용
    public void addCurrentNums(){
        if(currentNums < limitedNums) currentNums++;
        if(currentNums == limitedNums) changeStatusToFull();
    }

    //소셜링 취소, 강퇴 시 사용
    public void minusCurrentNums(){
        if(currentNums > 0) currentNums--;
    }

    public void changeStatusToFull(){
        status = SocialStatus.FULL;
    }

    public void changeStatusExpiration() {
        this.status = SocialStatus.EXPIRATION;
    }

    @Builder
    public Social(User user, List<PostImage> images, List<Comment> comments, String contents, int likes, Category category,
                  List<SocialTag> socialTags, List<Socialing> socialings, SocialStatus status, String title, Integer hits,
                  LocalDateTime startDate, LocalDateTime endDate, Integer currentNums, Integer limitedNums, String contact) {
        super(user, images, comments, contents, likes);
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
