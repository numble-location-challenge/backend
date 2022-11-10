package com.example.backend.domain.post;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.backend.domain.Comment;
import com.example.backend.domain.PostImage;
import com.example.backend.domain.User;
import com.example.backend.domain.tag.PostCategory;

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

    @Builder
    public Feed(User user, List<PostImage> images, List<Comment> comments,
        List<PostCategory> category, String contents, Integer region, int likes,
        Social social) {
        super(user, images, comments, category, contents, region, likes);
        this.social = social;
    }

    public void updateFeed(String contents, Social social, List<PostImage> images, Integer region){
        this.images = images;
        this.contents = contents;
        this.social = social;
        this.region = region;
    }
}
