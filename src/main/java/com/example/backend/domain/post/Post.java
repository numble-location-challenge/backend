package com.example.backend.domain.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.example.backend.domain.Comment;
import com.example.backend.domain.PostImage;
import com.example.backend.domain.TimeAuditingEntity;
import com.example.backend.domain.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Entity
@Table(name = "POST")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "post_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public abstract class Post extends TimeAuditingEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user; //작성자

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    protected List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @NotNull
    protected String contents; //내용

    @NotNull
    @Column(name = "region_code")
    protected Integer regionCode; //시군구 5자리

    @NotNull
    @Column(name = "dong_code")
    protected Long dongCode; //읍면동 (자릿수 유동적)

    @NotNull
    @Column(name = "region_name")
    protected String dongName; //행정구역명

    @NotNull
    private int likes; //좋아요 수

    public Post(User user, List<PostImage> images, List<Comment> comments, String contents, int likes) {
        this.user = user;
        this.images = images;
        this.comments = comments;
        this.contents = contents;
        this.dongCode = user.getDongCode();
        this.dongName = user.getDongName();
        this.regionCode = user.getRegionCodeFromDongCode();
        this.likes = likes;
    }

    public void updateLikeCnt(int cnt){
        this.likes = cnt;
    }
}
