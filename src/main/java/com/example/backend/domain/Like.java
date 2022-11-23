package com.example.backend.domain;

import com.example.backend.domain.post.Post;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "LIKES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Like {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; //좋아요 누른 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; //좋아요 단 글


    @Builder
    private Like(User user, Post post){
        this.user = user;
        this.post = post;
    }

    public static Like createLike(User user, Post post){
        Like like = Like.builder().user(user).post(post).build();
        return like;
    }
    //postType
}
