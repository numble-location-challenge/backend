package com.example.backend.domain.post;

import com.example.backend.domain.Comment;
import com.example.backend.domain.PostImage;
import com.example.backend.domain.TimeAuditingEntity;
import com.example.backend.domain.User;
import com.example.backend.domain.tag.PostCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "POST")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "post_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class Post extends TimeAuditingEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; //작성자

    @OneToMany(mappedBy = "post")
    private List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<PostCategory> category;

    @NotNull
    private String contents; //내용
    @NotNull
    private Integer region; //지역 optional

    private int likes; //좋아요 수

    public Post(User user, List<PostImage> images, List<Comment> comments, List<PostCategory> category, String contents, Integer region, int likes) {
        this.user = user;
        this.images = images;
        this.comments = comments;
        this.category = category;
        this.contents = contents;
        this.region = region;
        this.likes = likes;
    }
}
