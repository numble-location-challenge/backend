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

@Tag(name = "socialing", description = "유저-모임 API")
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
        socialingService.addParticipants(userId, socialId);
        return ResponseDTO.builder().success(true).message("모임 신청 처리되었습니다.").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 신청취소")
    @DeleteMapping("/socialing/{socialId}")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> cancel(@AuthenticationPrincipal Long userId, @PathVariable Long socialId) {
        socialingService.deleteParticipants(userId, socialId);
        return ResponseDTO.builder().success(true).message("모임 신청이 취소되었습니다.").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 강퇴")
    @PostMapping("/socialing/{socialId}/{kickedUserId}")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> kickOutUser(@AuthenticationPrincipal Long userId, @PathVariable Long socialId, @PathVariable Long kickedUserId) {
        socialingService.kickOutParticipants(userId, socialId, kickedUserId);
        return ResponseDTO.builder().success(true).message("해당 유저가 강퇴처리되었습니다.").build();
    }
}