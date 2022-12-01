package com.example.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.backend.domain.post.Post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "COMMENT")
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
    @Column(name = "c_group")
    private int cGroup; // group
    private int level; // 계층
    @Column(name = "ref_order")
    private int refOrder; // 같은 그룹 내의 순서
    @Column(name = "parent_num")
    private Long parentNum; // 부모 댓글의 ID
    private Boolean deleted = false; //삭제되었는지 여부

    @Builder
    private Comment(User user, Post post, String contents, int cGroup, int level, int refOrder,
        Long parentNum) {
        this.user = user;
        this.post = post;
        this.contents = contents;
        this.cGroup = cGroup;
        this.level = level;
        this.refOrder = refOrder;
        this.parentNum = parentNum;
    }

    public static Comment createComment(String contents, int cGroup, int level, int refOrder,
        Long parentNum, Post post, User user){

        Comment comment = Comment.builder()
            .contents(contents)
            .cGroup(cGroup)
            .level(level)
            .refOrder(refOrder)
            .parentNum(parentNum)
            .build();
        comment.setPost(post);
        comment.setUser(user);
        return comment;
    }
    public void setPost(Post post){
        this.post = post;
        post.getComments().add(this);
    }
    public void setUser(User user){
        this.user = user;
    }

    public void setDeleted(){
        this.deleted = true;
    }

    public void updateComment(String contents){
        this.contents = contents;
    }
}
