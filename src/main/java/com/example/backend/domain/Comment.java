package com.example.backend.domain;

import com.example.backend.domain.post.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@Table(name = "COMMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment extends TimeAuditingEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; //작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 댓글이 달린 글

    @NotNull
    private String contents;
    private int likes;
    private int cGroup; // group
    private int level; // 계층
    private int refOrder; // 같은 그룹 내의 순서
    private Long parentNum; // 부모 댓글의 ID


    //postType

}
