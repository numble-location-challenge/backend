package com.example.backend.controller;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.social.SocialCreateRequestDTO;
import com.example.backend.dto.social.SocialLongDTO;
import com.example.backend.dto.social.SocialModifyRequestDTO;
import com.example.backend.dto.social.SocialShortDTO;
import com.example.backend.global.exception.social.SocialInvalidInputException;
import com.example.backend.global.exception.social.SocialInvalidInputExceptionType;
import com.example.backend.service.social.SocialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@Tag(name = "social", description = "모임 게시글 API")
@RestController
@RequiredArgsConstructor
public class SocialController {
    private final SocialService socialService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 리스트 출력", description = "간략한 정보만 제공")
    @GetMapping("/social")
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
    @Operation(summary = "모임 상세 정보 출력 (자세히)", description = "모임 게시글 한 개 클릭시")
    @GetMapping("/social/{socialId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<SocialLongDTO> getSocialDetail(@Parameter(required = true) @PathVariable Long socialId){
        List<SocialLongDTO> socialLongDTOList = new LinkedList<>();
        socialLongDTOList.add(socialService.getSocialDetail(socialId));

        return ResponseDTO.<SocialLongDTO>builder().success(true).message("모임 상세 정보 출력")
                .data(socialLongDTOList).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 게시글 생성", description = "contact 제외한 나머지 변수 모두 not null, tags와 images는 1개~3개 가능")
    @PostMapping("/social")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> createSocial(
            @AuthenticationPrincipal String email,
            @RequestBody final SocialCreateRequestDTO socialCreateRequestDTO){
        socialService.createSocial(email, socialCreateRequestDTO);
        return ResponseDTO.builder().success(true).message("모임 생성 완료").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 게시글 수정", description = "모든 변수 null available")
    @PutMapping("/social/{socialId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> modifySocial(
            @AuthenticationPrincipal String email,
            @PathVariable Long socialId,
            @RequestBody final SocialModifyRequestDTO socialModifyRequestDTO){
        socialService.modifySocial(email, socialId, socialModifyRequestDTO);
        return ResponseDTO.builder().success(true).message("모임 수정 완료").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 게시글 삭제")
    @DeleteMapping("/social/{socialId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> deleteSocial(@Parameter(required = true) @PathVariable Long socialId){
        socialService.deleteSocial(socialId);
        return ResponseDTO.builder().success(true).message("모임 삭제 완료").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "내가 쓴 글 보기", description = "본인이 작성한 모임 게시글만 출력")
    @GetMapping("/social/me/{userId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<SocialShortDTO> getMySocialList(@Parameter(required = true) @PathVariable Long userId){
        List<SocialShortDTO> socialShortDTOList = socialService.getMySocialList(userId);

        return ResponseDTO.<SocialShortDTO>builder().success(true).message("내가 쓴 모임 게시글 출력")
                .data(socialShortDTOList).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "내가 참여한 모임 보기", description = "본인이 참여한 모임 게시글만 출력")
    @GetMapping("/social/join/{userId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<SocialShortDTO> getJoinSocialList(@Parameter(required = true) @PathVariable Long userId){
        List<SocialShortDTO> socialShortDTOList = socialService.getJoinSocialList(userId);

        return ResponseDTO.<SocialShortDTO>builder().success(true).message("내가 쓴 모임 게시글 출력")
                .data(socialShortDTOList).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 게시글 태그로 검색하기", description = "")
    @GetMapping("/social/search/{tagNum}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<SocialShortDTO> getFilteringList(@Parameter(required = true) @PathVariable Long tagNum){
        List<SocialShortDTO> socialShortDTOList = socialService.filteringByTag(tagNum);

        return ResponseDTO.<SocialShortDTO>builder().success(true).message("필터링된 리스트 출력")
                .data(socialShortDTOList).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 게시글 정렬하기", description = "sortNum = 1:최신순, 2:마감임박순, 3:인기순")
    @GetMapping("/social/sort/{sortNum}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<SocialShortDTO> getSortList(@Parameter(required = true) @PathVariable int sortNum){
        String properties = "";
        Boolean sortDirection = false; // true : DESC, false : ASC
        String message = "";
        switch (sortNum){
            case 1:
                sortDirection = true;
                properties = "createDate";
                message = "최신순 정렬";
                break;
            case 2:
                sortDirection = false;
                properties = "endDate";
                message = "마감 임박순 정렬";
                break;
            case 3:
                sortDirection = true;
                properties = "likes";
                message = "인기순 정렬";
                break;
            default:
                throw new SocialInvalidInputException(SocialInvalidInputExceptionType.OUT_OF_RANGE_OF_INPUT);
        }
        List<SocialShortDTO> socialShortDTOList = socialService.sortByList(sortDirection, properties);

        return ResponseDTO.<SocialShortDTO>builder().success(true).message(message)
                .data(socialShortDTOList).build();
    }

}

