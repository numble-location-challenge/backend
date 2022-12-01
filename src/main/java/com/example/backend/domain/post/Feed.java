package com.example.backend.domain.post;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.backend.domain.Comment;
import com.example.backend.domain.PostImage;
import com.example.backend.domain.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "FEED")
@DiscriminatorValue("F")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends Post {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_id")
    private Social social; //작성자가 참여 중인 모임 연결

    @Transient
    private boolean liked;

    @Builder
    private Feed(User user, List<PostImage> images, List<Comment> comments,
        String contents, int likes, Social social) {
        super(user, images, comments, contents, likes);
        this.social = social;
    }

    public static Feed createFeed(User user, List<PostImage> feedImages,
        String contents, Social social){

        Feed feed = Feed.builder()
            .contents(contents)
            .user(user)
            .likes(0)
            .social(social)
            .build();
        feed.setImages(feedImages);

        return feed;
    }

    public int getCommentCount(){
        return this.getComments().size();
    }

    public void setImages(List<PostImage> postImages){
        for (PostImage postImage : postImages){
            postImage.setPost(this);
        }
        this.images = postImages;
    }

    public void updateFeed(String contents, Social social, List<PostImage> images){
        this.setImages(images);
        this.contents = contents;
        this.social = social;
    }

    public void setLiked(boolean liked){
        this.liked = liked;
    }

    public boolean isLiked() {
        return this.liked;
    }
}
