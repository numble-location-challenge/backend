package com.example.backend.dto.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class SocialModifyRequestDTO {

    @Schema(description = "모임 제목", defaultValue = "헬스해서 지구뿌수자")
    String title;

    @Schema(description = "모임 설명", defaultValue = "프로틴프로틴프로틴프로틴")
    String contents;

    @Schema(description = "모임 시작 날짜", defaultValue = "yyyy-MM-dd")
    private LocalDateTime startDate; //모임 시작날짜

    @Schema(description = "모임 종료 날짜", defaultValue = "yyyy-MM-dd")
    private LocalDateTime endDate;

    @Schema(description = "최대 모집 인원", defaultValue = "8")
    private Integer limitedNums; //최대 모집 인원수

    @Schema(description = "모임 주최자 연락 방법", defaultValue = "https://openkakao/~")
    private String contact; //연락 방법

    @Schema(description = "배경 이미지 리스트 1~3개",  defaultValue = "/social/files/?.jpeg")
    private List<String> images = new ArrayList<>();

}
