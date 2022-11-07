package com.example.backend.dto;

import com.example.backend.domain.Socialing;
import com.example.backend.domain.enumType.SocialStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SocialDTO {
    private List<Socialing> socialings = new ArrayList<>();
    private Long socialId; //소셜 포스트 id
    private String title; //모임 제목
    private int hits; //조회수
    private LocalDateTime startDate; //모임 시작날짜
    private LocalDateTime endDate; //모임 끝나는 날짜
    private int currentNums; //현재 신청한 인원수
    private int limitedNums; //최대 모집 인원수
    private SocialStatus status; //신청 가능 유무
    private String contact; //연락 방법

}
