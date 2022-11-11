package com.example.backend.controller;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.social.SocialLongDTO;
import com.example.backend.dto.social.SocialShortDTO;
import com.example.backend.service.SocialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "social", description = "모임 게시글 API")
@RestController
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 리스트 출력 (미리보기)")
    @GetMapping("/socials")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<SocialShortDTO> getSocialList(){
        List<SocialShortDTO> socialShortDTOList = socialService.getSocialList();

        return ResponseDTO.<SocialShortDTO>builder().success(true).message("모임 리스트 출력")
                .data(socialShortDTOList).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 상세 정보 출력 (자세히)")
    @GetMapping("/social/{socialId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<SocialLongDTO> getSocial(@Parameter(required = true) @PathVariable Long socialId){
        SocialLongDTO socialLongDTO = socialService.getSocial(socialId);
        List<SocialLongDTO> socialLongDTOList = List.of(SocialLongDTO.builder()
                        .id(socialLongDTO.getId())
                        .user(socialLongDTO.getUser())
                        .region(socialLongDTO.getRegion())
                        .comments(socialLongDTO.getComments())
                        .images(socialLongDTO.getImages())
                        .contents(socialLongDTO.getContents())
                        .socialings(socialLongDTO.getSocialings())
                        .title(socialLongDTO.getTitle())
                        .startDate(socialLongDTO.getStartDate())
                        .endDate(socialLongDTO.getEndDate())
                        .hits(socialLongDTO.getHits())
                        .contact(socialLongDTO.getContact())
                        .tagId(socialLongDTO.getTagId())
                        .build());
        return ResponseDTO.<SocialLongDTO>builder().success(true).message("모임 단건 조회").data(socialLongDTOList).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 게시글 생성")
    @PostMapping("/social")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<?> createSocial(@RequestBody final SocialLongDTO socialLongDTO){
        socialService.createSocial(socialLongDTO);
        return ResponseDTO.builder().success(true).message("모임 생성 완료").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 게시글 수정")
    @PutMapping("/social")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<?> updateSocial(@RequestBody final SocialLongDTO socialLongDTO){
        socialService.updateSocial(socialLongDTO);
        return ResponseDTO.builder().success(true).message("모임 수정 완료").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 게시글 삭제")
    @DeleteMapping("/social/{socialId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<?> deleteSocial(@Parameter(required = true) @PathVariable Long socialId){
        socialService.deleteSocial(socialId);
        return ResponseDTO.builder().success(true).message("모임 삭제 완료").build();
    }
}
