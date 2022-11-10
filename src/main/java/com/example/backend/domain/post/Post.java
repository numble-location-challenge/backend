package com.example.backend.domain.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.example.backend.domain.Comment;
import com.example.backend.domain.PostImage;
import com.example.backend.domain.TimeAuditingEntity;
import com.example.backend.domain.User;
import com.example.backend.domain.tag.PostCategory;

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
    private User user; //작성자

    @OneToMany(mappedBy = "post")
    protected List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @NotNull
    protected String contents; //내용

    @NotNull
    protected Integer region; //지역 optional

    private int likes; //좋아요 수

    public Post(User user, List<PostImage> images, List<Comment> comments, String contents, Integer region, int likes) {
        this.user = user;
        this.images = images;
        this.comments = comments;
        this.contents = contents;
        this.region = region;
        this.likes = likes;
    }
}
