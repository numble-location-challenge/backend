package com.example.backend.controller;

import com.example.backend.domain.post.Social;
import com.example.backend.dto.response.ResponseDTO;
import com.example.backend.dto.social.SocialCreateRequestDTO;
import com.example.backend.dto.social.SocialLongDTO;
import com.example.backend.dto.social.SocialModifyRequestDTO;
import com.example.backend.dto.social.SocialShortDTO;
import com.example.backend.global.security.CustomUserDetails;
import com.example.backend.service.social.SocialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "social", description = "모임 게시글 API")
@RestController
@RequiredArgsConstructor
public class SocialController {
    private final SocialService socialService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 리스트 출력", description = "간략한 정보만 제공 (userId로 사용자의 동정보를 가져와 행정구역으로 필터링)")
    @GetMapping("/social")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<?> getSocialList(@AuthenticationPrincipal CustomUserDetails user){
        List<SocialShortDTO> socialShortDTOList = socialService.getSocialList(user.getUserId());

        return new ResponseDTO<>(socialShortDTOList, "모임 리스트 출력");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 상세 정보 출력 (자세히)", description = "모임 게시글 한 개 클릭시")
    @GetMapping("/social/{socialId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> getSocialDetail(@AuthenticationPrincipal CustomUserDetails user, @Parameter(required = true) @PathVariable Long socialId){
        SocialLongDTO socialLongDTO = socialService.getSocialDetail(user.getUserId(),socialId);

        return new ResponseDTO<>(socialLongDTO, "모임 상세 정보 출력");
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
    public ResponseEntity<?> createSocial(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody final SocialCreateRequestDTO socialCreateRequestDTO, UriComponentsBuilder uriBuilder){
        Social createdSocial = socialService.createSocial(user.getEmail(), socialCreateRequestDTO);

        URI location = uriBuilder.path("/social/{id}")
                .buildAndExpand(createdSocial.getId()).toUri();

        return ResponseEntity.created(location)
                .header(HttpHeaders.LOCATION, location.toString())
                .body(new ResponseDTO<>(null, "모임 생성 완료"));
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
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long socialId,
            @RequestBody final SocialModifyRequestDTO socialModifyRequestDTO){
        Social modifiedSocial = socialService.modifySocial(user.getEmail(), socialId, socialModifyRequestDTO);
        SocialLongDTO socialLongDTO = new SocialLongDTO(modifiedSocial);
        return new ResponseDTO<>(socialLongDTO, "모임 수정 완료");
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
        return new ResponseDTO<>(null, "모임 삭제 완료");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "내가 쓴 글 보기", description = "본인이 작성한 모임 게시글만 출력")
    @GetMapping("/social/me")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<?> getMySocialList(@Parameter(required = true) @AuthenticationPrincipal CustomUserDetails user){
        List<SocialShortDTO> socialShortDTOList = socialService.getMySocialList(user.getUserId());

        return new ResponseDTO<>(socialShortDTOList,"내가 쓴 모임 게시글 출력");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "내가 참여한 모임 보기", description = "본인이 참여한 모임 게시글만 출력")
    @GetMapping("/social/join")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseDTO<?> getJoinSocialList(@Parameter(required = true) @AuthenticationPrincipal CustomUserDetails user){
        List<SocialShortDTO> socialShortDTOList = socialService.getJoinSocialList(user.getUserId());

        return new ResponseDTO<>(socialShortDTOList,"내가 참가한 모임들 출력");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 게시글 태그로 검색하기", description = "")
    @GetMapping("/social/search/{tagNum}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST")
    })
    public ResponseDTO<?> getFilteringList(@Parameter(required = true) @PathVariable Long tagNum){
        List<SocialShortDTO> socialShortDTOList = socialService.filteringByTag(tagNum);

        return new ResponseDTO<>(socialShortDTOList,"검색 결과 출력");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 전체 게시글 정렬하기", description = "sortType : 정렬 타입 (1 : 최신순, 2 : 마감 임박순, 3 : 인기순)")
    @GetMapping("/social/sort/{sortType}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST")
    })
    public ResponseDTO<?> getSortList(@AuthenticationPrincipal CustomUserDetails user,@Parameter(required = true) @PathVariable int sortType){
        List<SocialShortDTO> socialList = socialService.getSocialList(user.getUserId()); //정렬할 리스트

        List<SocialShortDTO> sortedList = socialService.sortByList(socialList, sortType); //정렬된 리스트
        String message = (sortType == 1 ? "최신순 정렬" : (sortType == 2) ? "마감 임박순 정렬" : "인기순 정렬");
        return new ResponseDTO<>(sortedList,message);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "카테고리별 모임 정렬하기", description = "categoryId : 카테고리 id, sortType = (1 : 최신순, 2 : 마감 임박순, 3 : 인기순)")
    @GetMapping("/social/{categoryId}/sort/{sortType}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST")
    })
    public ResponseDTO<?> getSortCategoryList(@AuthenticationPrincipal CustomUserDetails user,@Parameter(required = true) @PathVariable Long categoryId, @PathVariable int sortType){
        List<SocialShortDTO> categoryList = socialService.filteringByCategory(user.getUserId(),categoryId); //정렬할 리스트

        List<SocialShortDTO> sortedList = socialService.sortByList(categoryList, sortType); //정렬된 리스트

        String message = (sortType == 1 ? "최신순 정렬" : (sortType == 2) ? "마감 임박순 정렬" : "인기순 정렬");

        return new ResponseDTO<>(sortedList,message);
    }


}

