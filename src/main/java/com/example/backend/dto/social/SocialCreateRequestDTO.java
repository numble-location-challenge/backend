package com.example.backend.dto.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class SocialCreateRequestDTO {

    //지역번호는 user에서 가져옴

    @Schema(description = "모임 제목", defaultValue = "헬스해서 지구뿌수자")
    @NotNull
    String title;

    @Schema(description = "모임 설명", defaultValue = "프로틴프로틴프로틴프로틴")
    @NotNull
    String contents;

    @Schema(description = "모임 시작 날짜", defaultValue = "yyyy-MM-dd")
    @NotNull
    private LocalDateTime startDate; //모임 시작날짜

    @Schema(description = "모임 종료 날짜", defaultValue = "yyyy-MM-dd")
    @NotNull
    private LocalDateTime endDate;

    @Schema(description = "최대 모집 인원", defaultValue = "8")
    @NotNull
    private int limitedNums; //최대 모집 인원수

    @Schema(description = "모임 주최자 연락 방법", defaultValue = "https://openkakao/~")
    private String contact; //연락 방법

    @Schema(description = "List<String> 소분류 태그 1~3개", defaultValue = "헬스")
    @NotNull
    private List<String> tags = new ArrayList<>();

    @Schema(description = "List<String> 배경 이미지 리스트 1~3개", defaultValue = "/social/files/?.jpeg")
    @NotNull
    private List<String> images = new ArrayList<>();

}
