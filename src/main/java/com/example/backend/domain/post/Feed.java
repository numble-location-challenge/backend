package com.example.backend.domain.post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "FEED")
@DiscriminatorValue("F")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Feed extends Post {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_id")
    private Social social; //작성자가 참여 중인 모임 연결

}
