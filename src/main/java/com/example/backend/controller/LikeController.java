package com.example.backend.controller;

import com.example.backend.dto.LikesDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Like", description = "게시글 좋아요 API")
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "좋아요 리스트 출력", description = "해당 사용자가 누른 좋아요만 제공")
    @GetMapping("/like/{userId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> getLikeList(@Parameter(required = true) @PathVariable Long userId){
        List<LikesDTO> likesDTOList = likeService.getLikes(userId);

        return new ResponseDTO<>(likesDTOList,"좋아요 리스트 출력");
//        return ResponseDTO.<LikesDTO>builder().success(true).message("좋아요 리스트 출력")
//                .data(likesDTOs).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "좋아요 선택", description = "사용자가 좋아요 누를 시")
    @PostMapping("/like/{userId}/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<LikesDTO> onLike(@Parameter(required = true) @PathVariable Long userId,@PathVariable Long postId){
        LikesDTO likesDTO = likeService.onLike(userId,postId);

        return new ResponseDTO<>(likesDTO,"좋아요 on");
//        return ResponseDTO.<LikesDTO>builder().success(true).message("좋아요 on")
//                .data(likesDTOs).build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "좋아요 선택", description = "사용자가 좋아요 누를 시")
    @DeleteMapping("/like/{userId}/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> offLike(@Parameter(required = true) @PathVariable Long userId,@PathVariable Long postId){
        likeService.offLike(userId,postId);
        return new ResponseDTO<>(null,"좋아요 off");
//        return ResponseDTO.builder().success(true).message("좋아요 off").build();
    }
}
