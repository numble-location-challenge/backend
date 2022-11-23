package com.example.backend.domain.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.example.backend.domain.Comment;
import com.example.backend.domain.PostImage;
import com.example.backend.domain.TimeAuditingEntity;
import com.example.backend.domain.User;

import com.example.backend.domain.enumType.SocialStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "POST")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "post_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @NotNull
    protected String contents; //내용

    @NotNull
    protected Integer region; //지역 optional

    @NotNull
    private int likes; //좋아요 수

    public Post(User user, List<PostImage> images, List<Comment> comments, String contents, Integer region, int likes) {
        this.user = user;
        this.images = images;
        this.comments = comments;
        this.contents = contents;
        this.region = region;
        this.likes = likes;
    }

    public void updateLikeCnt(int cnt){
        this.likes = cnt;
    }
}
