package com.example.backend.controller;

import com.example.backend.dto.LikesDTO;
import com.example.backend.dto.response.ResponseDTO;
import com.example.backend.global.security.CustomUserDetails;
import com.example.backend.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Like", description = "게시글 좋아요 API")
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "좋아요 클릭", description = "사용자가 좋아요 누를 시")
    @PostMapping("/like/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> setLike(@AuthenticationPrincipal CustomUserDetails user, @Parameter(required = true) @PathVariable Long postId){

        LikesDTO likesDTO = likeService.likeOnAndOff(user.getUserId(),postId);
        String message = likesDTO.isOrElse() ? "좋아요 등록" : "좋아요 해제";

        return new ResponseDTO<>(likesDTO,message);
    }

}
