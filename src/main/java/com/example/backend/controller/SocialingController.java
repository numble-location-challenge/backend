package com.example.backend.controller;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.service.SocialingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "login/logout", description = "로그인 API")
@RestController
@RequiredArgsConstructor
public class SocialingController {

    private final SocialingService socialingService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 신청")
    @PostMapping("/socialing/{socialId}")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> apply(@AuthenticationPrincipal Long userId, @PathVariable Long socialId) {
        socialingService.addApplyUser(userId, socialId);
        return ResponseDTO.builder().success(true).message("모임 신청 처리되었습니다.").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 신청취소")
    @PostMapping("/socialing/{socialId}")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> cancel(@AuthenticationPrincipal Long userId, @PathVariable Long socialId) {
        socialingService.deleteUserApplied(userId, socialId);
        return ResponseDTO.builder().success(true).message("모임 신청이 취소되었습니다.").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 강퇴")
    @PostMapping("/socialing/{socialId}/{droppedUserId}")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> dropUser(@AuthenticationPrincipal Long userId, @PathVariable Long socialId, @PathVariable Long droppedUserId) {
        socialingService.dropSocialUser(userId, socialId, droppedUserId);
        return ResponseDTO.builder().success(true).message("해당 유저가 강퇴처리되었습니다.").build();
    }
}
