package com.example.backend.domain;

import com.example.backend.domain.post.Social;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "SOCIALING")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Socialing {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "socialing_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_id")
    private Social social;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //==생성 메서드==/
    public static Socialing createSocialing(User user, Social social){
        Socialing socialing = new Socialing();
        socialing.setUser(user);
        socialing.setSocial(social);
        //모임장도 참가한 사람 +1
        social.addCurrentNums();
        return socialing;
    }

    //==연관관계 메서드==//

    public void setUser(User user) {
        this.user = user;
    }

    public void setSocial(Social social){
        this.social = social;
    }
}
