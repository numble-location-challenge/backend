package com.example.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.post.Feed;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.feed.FeedHotPreviewDTO;
import com.example.backend.dto.feed.FeedListResponseDTO;
import com.example.backend.dto.feed.FeedPagingDTO;
import com.example.backend.dto.feed.FeedRequestDTO;
import com.example.backend.dto.feed.FeedResponseDTO;
import com.example.backend.dto.feed.FeedSearch;
import com.example.backend.dto.feed.MyFeedResponseDTO;
import com.example.backend.service.feed.FeedService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "feed", description = "피드 API")
@Slf4j
@RequiredArgsConstructor
@RestController
public class FeedController {

    private final FeedService feedService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "피드 단건조회", description = "피드의 ID를 이용하여 피드를 단건 조회합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @GetMapping("/posts/{postId}")
    public ResponseDTO<?> getFeed(@PathVariable("postId") Long postId) {
        Feed feed = feedService.getFeed(postId);
        FeedResponseDTO result = new FeedResponseDTO(feed);
        return new ResponseDTO<>(result,"피드 단건 조회 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "피드 리스트 조회 가장 최근의 댓글(대댓글X)를 미리보기로 보여준다.(없을 시 null)", description = "검색조건으로 피드 리스트를 조회합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @GetMapping("/posts")
    public FeedPagingDTO<?> getFeeds(@Valid @ModelAttribute FeedSearch feedSearch, @AuthenticationPrincipal String userEmail) {
        Slice<Feed> feeds = feedService.getFeeds(feedSearch, "hello4@numble.com");
        List<FeedListResponseDTO> result = feeds.stream()
            .map(feed -> new FeedListResponseDTO(feed))
            .collect(Collectors.toList());
        log.info("size = {}", result.size());
        return new FeedPagingDTO<>(result,"피드 리스트 조회 성공",feeds.hasNext());
    }
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "내가 작성한 피드 리스트 조회", description = "내가 작성한 피드 리스트를 조회합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @GetMapping("/posts/me")
    public ResponseDTO<?> getMyFeeds(@AuthenticationPrincipal String userEmail){
        List<Feed> feeds = feedService.getMyFeeds("hello4@numble.com");
        List<MyFeedResponseDTO> result = feeds.stream()
            .map(feed -> new MyFeedResponseDTO(feed))
            .collect(Collectors.toList());
        return new ResponseDTO<>(result,"나의 피드 리스트 조회 성공");
    }
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "홈 화면에서의 인기있는 피드 리스트 미리보기 조회 10개", description = "홈화면에서 인기 있는 피드 리스트 10개 미리보기")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @GetMapping("/posts/hot")
    public ResponseDTO<?> getHotPreviewFeeds(@AuthenticationPrincipal String userEmail){
        List<Feed> feeds = feedService.getHotPreviewFeeds("hello4@numble.com");
        List<FeedHotPreviewDTO> result = feeds.stream().map(feed -> new FeedHotPreviewDTO(feed))
            .collect(Collectors.toList());

        return new ResponseDTO<>(result,"인기 리스트 조회 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "피드 생성", description = "사용자가 피드를 작성 후 생성합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    @PostMapping("/posts")
    public ResponseDTO<?> createFeed(@Valid @RequestBody FeedRequestDTO feedRequestDTO, @AuthenticationPrincipal String userEmail) {
        feedService.createFeed(feedRequestDTO, "hello4@numble.com");
        return new ResponseDTO<>(null,"피드 생성 성공");

    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "피드 삭제", description = "자신의 피드를 삭제합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @DeleteMapping("/posts/{postId}")
    public ResponseDTO<?> deleteFeed(@PathVariable Long postId, @AuthenticationPrincipal String userEmail) {
        feedService.deleteFeed(postId, "hello4@numble.com");
        return new ResponseDTO<>(null,"피드 삭제 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "피드 수정", description = "자신의 피드를 수정합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @PutMapping("/posts/{postId}")
    public ResponseDTO<?> updateFeed(@PathVariable Long postId, @Valid @RequestBody FeedRequestDTO feedRequestDTO, @AuthenticationPrincipal String userEmail) {
        Feed feed = feedService.updateFeed(postId, feedRequestDTO, "hello4@numble.com");
        FeedResponseDTO result = new FeedResponseDTO(feed);
        return new ResponseDTO<>(result, "피드 수정 성공");
    }
}
