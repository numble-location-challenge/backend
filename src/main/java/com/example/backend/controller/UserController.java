package com.example.backend.controller;

import com.example.backend.domain.User;
import com.example.backend.dto.*;
import com.example.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "user", description = "회원 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원가입")
    @PostMapping("/join")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    public ResponseDTO<?> join(@RequestBody final UserJoinRequestDTO userJoinRequestDTO){
        //TODO validation 처리
        userService.createDefaultUser(userJoinRequestDTO);
        return ResponseDTO.builder().success(true).message("회원가입 처리되었습니다.").build();
    }

    //TODO 서비스 구현, jwt 적용 전
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/user/delete")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> deleteUser(@AuthenticationPrincipal Long id){

        userService.delete(id);
        return ResponseDTO.builder().success(true).message("정상 탈퇴되었습니다.").build();
    }

    //TODO 서비스 구현, jwt 적용 전
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원 수정")
    @PutMapping("/user/me")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<UserProfileDTO> modifyUser(
            @AuthenticationPrincipal Long id,
            @RequestBody final UserModifyRequestDTO userModifyRequestDTO){

        User user = userService.modifyUser(id, userModifyRequestDTO);
        //set data list
        List<UserProfileDTO> users = List.of(UserProfileDTO.builder()
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .region(user.getRegion())
                .bio(user.getBio())
                .build());

        return ResponseDTO.<UserProfileDTO>builder().success(true).message("정상 수정 처리 되었습니다.")
                .data(users).build();
    }

    //TODO 서비스 구현, jwt 적용 전
    //내 프로필 조회할 때 피드, 댓글도 같이 들고 와야하는지? 일단 빼고 처리
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원 프로필 조회")
    @GetMapping("/user/me")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<UserProfileDTO> getUserInfo(@AuthenticationPrincipal Long id){

        User user = userService.getUser(id);
        //set data list
        List<UserProfileDTO> users = List.of(UserProfileDTO.builder()
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .region(user.getRegion())
                .bio(user.getBio())
                .build());

        return ResponseDTO.<UserProfileDTO>builder().success(true).message("프로필 조회 결과")
                .data(users).build();
    }
}
