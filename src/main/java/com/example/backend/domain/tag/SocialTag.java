package com.example.backend.domain.tag;

import com.example.backend.domain.post.Social;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "SOCIAL_TAG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SocialTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_id")
    private Social social;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    //==생성 메서드==//
    public static SocialTag createSocialTag(Social social, Tag tag) {
        SocialTag socialTag = new SocialTag();
        socialTag.setSocial(social);
        socialTag.setTag(tag);
        return socialTag;
    }

    //==연관관계 메서드==//
    public void setSocial(Social social){
        this.social = social;
    }

    public void setTag(Tag tag){
        this.tag = tag;
    }

}
