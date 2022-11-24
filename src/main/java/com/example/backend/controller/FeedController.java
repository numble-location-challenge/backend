package com.example.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.domain.post.Feed;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.feed.FeedListResponseDTO;
import com.example.backend.dto.feed.FeedRequestDTO;
import com.example.backend.dto.feed.FeedResponseDTO;
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
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @GetMapping("/posts/{postId}")
    public ResponseDTO<?> getFeed(@PathVariable("postId") Long postId) {
        Feed feed = feedService.getFeed(postId);
        FeedResponseDTO result = new FeedResponseDTO(feed);
        return new ResponseDTO<>(result,"피드 단건 조회 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "피드 리스트 조회", description = "검색조건으로 피드 리스트를 조회합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @GetMapping("/posts")
    public ResponseDTO<?> getFeeds(@RequestParam String search) {
        List<Feed> feeds = feedService.getFeeds(search);

        List<FeedListResponseDTO> result = feeds.stream()
            .map(feed -> new FeedListResponseDTO(feed))
            .collect(Collectors.toList());
        log.info("size = {}", result.size());
        return new ResponseDTO<>(result,"피드 리스트 조회 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "피드 생성", description = "사용자가 피드를 작성 후 생성합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    @PostMapping("/posts")
    public ResponseDTO<?> createFeed(@RequestBody FeedRequestDTO feedRequestDTO) {
        feedService.createFeed(feedRequestDTO, 1L);
        return new ResponseDTO<>(null,"피드 생성 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "피드 삭제", description = "자신의 피드를 삭제합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @DeleteMapping("/posts/{postId}")
    public ResponseDTO<?> deleteFeed(@PathVariable Long postId) {
        Long userId = 1L;
        feedService.deleteFeed(postId, userId);
        return new ResponseDTO<>(null,"피드 삭제 성공");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "피드 수정", description = "자신의 피드를 수정합니다.")
    @ApiResponses(
        {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @PutMapping("/posts/{postId}")
    public ResponseDTO<?> updateFeed(@PathVariable Long postId, @RequestBody FeedRequestDTO feedRequestDTO) {
        Long userId = 1L;
        Feed feed = feedService.updateFeed(postId, feedRequestDTO, userId);
        FeedResponseDTO result = new FeedResponseDTO(feed);
        return new ResponseDTO<>(result, "피드 수정 성공");
    }
}
