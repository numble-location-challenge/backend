package com.example.backend.domain.post;

import com.example.backend.domain.Comment;
import com.example.backend.domain.PostImage;
import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;
import com.example.backend.domain.enumType.SocialStatus;
import com.example.backend.domain.tag.PostCategory;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "SOCIAL")
@DiscriminatorValue("S")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Social extends Post {

    @OneToMany(mappedBy = "social")
    private List<Socialing> socialings = new ArrayList<>();

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private SocialStatus status;

    @NotNull
    private String title; //모임 제목
    @NotNull
    private Integer hits; //조회수

    @NotNull
    @Column(name = "start_date")
    private LocalDateTime startDate; //모임 시작날짜
    @NotNull
    @Column(name = "end_date")
    private LocalDateTime endDate; //모임 끝나는 날짜

    @NotNull
    @Column(name = "current_nums")
    private Integer currentNums; //현재 신청한 인원수
    @NotNull
    @Column(name = "limited_nums")
    private Integer limitedNums; //최대 모집 인원수

    private String contact; //연락 방법

    @Builder
    public Social(User user, List<PostImage> images, List<Comment> comments, List<PostCategory> category, String contents, Integer region, int likes, List<Socialing> socialings, SocialStatus status, String title, Integer hits, LocalDateTime startDate, LocalDateTime endDate, Integer currentNums, Integer limitedNums, String contact) {
        super(user, images, comments, category, contents, region, likes);
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
