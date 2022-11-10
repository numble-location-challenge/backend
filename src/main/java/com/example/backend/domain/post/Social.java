package com.example.backend.domain.post;

import com.example.backend.domain.Socialing;
import com.example.backend.domain.enumType.SocialStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "SOCIAL")
@DiscriminatorValue("S")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Social extends Post {

    @OneToMany(mappedBy = "social")
    private List<Socialing> socialings = new ArrayList<>();

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private SocialStatus status;

    @NotNull
    private String title; //모임 제목
    @NotNull
    private Integer hits; //조회수

    @NotNull
    private LocalDateTime startDate; //모임 시작날짜
    @NotNull
    private LocalDateTime endDate; //모임 끝나는 날짜

    @NotNull
    private Integer currentNums; //현재 신청한 인원수
    @NotNull
    private Integer limitedNums; //최대 모집 인원수

    private String contact; //연락 방법

}
