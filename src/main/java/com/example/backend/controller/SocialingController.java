package com.example.backend.controller;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.service.user.UserService;
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

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 참가")
    @PostMapping("/socialing/{socialId}")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> participateSocial(@AuthenticationPrincipal String email, @PathVariable Long socialId) {
        userService.participateSocial(email, socialId);
        return ResponseDTO.builder().success(true).message("모임 신청 처리되었습니다.").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 참가 취소")
    @DeleteMapping("/socialing/{socialId}")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> cancelParticipationOfSocial(@AuthenticationPrincipal String email, @PathVariable Long socialId) {
        userService.cancelSocialParticipation(email, socialId);
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
    public ResponseDTO<?> kickOutSocialUser(@AuthenticationPrincipal String email, @PathVariable Long socialId, @PathVariable Long kickedUserId) {
        userService.kickOutUserFromSocial(email, socialId, kickedUserId);
        return ResponseDTO.builder().success(true).message("해당 유저가 강퇴처리되었습니다.").build();
    }
}
