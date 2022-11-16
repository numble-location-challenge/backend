package com.example.backend.controller;

import com.example.backend.domain.User;
import com.example.backend.dto.*;
import com.example.backend.dto.login.KaKaoAuthRequestDTO;
import com.example.backend.dto.user.UserJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;
import com.example.backend.dto.user.UserProfileDTO;
import com.example.backend.global.exception.ForbiddenException;
import com.example.backend.global.exception.ForbiddenExceptionType;
import com.example.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "user", description = "회원 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원가입",
            description = "unique field 중복 시 errorCode -101(이메일), -102(닉네임), -103(이메일,닉네임)이 반환됩니다.")
    @PostMapping("/join")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    public ResponseEntity<?> join(@RequestBody final UserJoinRequestDTO userJoinRequestDTO, UriComponentsBuilder uriBuilder){
        final User createdUser = userService.createDefaultUser(userJoinRequestDTO);
        return getCreatedResponseEntity(uriBuilder, createdUser.getId());
    }

    //TODO 서비스 구현
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "카카오 회원가입")
    @PostMapping("/kakaojoin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<?> kakaoJoin(@RequestBody final KaKaoAuthRequestDTO authRequestDTO, UriComponentsBuilder uriBuilder){
        //프론트에서 회원정보 동의해주면 AT 가지고 정보요청
        final User createdUser = userService.createKakaoUser(authRequestDTO);
        return getCreatedResponseEntity(uriBuilder, createdUser.getId());
    }

    private ResponseEntity<?> getCreatedResponseEntity(UriComponentsBuilder uriBuilder, Long id) {
        URI location = uriBuilder.path("/user/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).body(ResponseDTO.builder()
                        .success(true).message("회원가입 처리되었습니다.")
                        .data(null)
                        .build());
    }

    //TODO 서비스 구현
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

    //TODO 서비스 구현
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원 수정", description = "모든 변수 null available")
    @PutMapping("/user/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<UserProfileDTO> modifyUser(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id,
            @RequestBody final UserModifyRequestDTO userModifyRequestDTO){

        if(userId.equals(id)) throw new ForbiddenException(ForbiddenExceptionType.USER_UN_AUTHORIZED);

        User user = userService.getUser(userId);
        //TODO 유저 수정

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

    //TODO 서비스 구현
    //내 프로필 조회할 때 피드, 댓글도 같이 들고 와야하는지? 일단 빼고 처리
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원 프로필 조회")
    @GetMapping("/user/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<UserProfileDTO> getUserInfo(@AuthenticationPrincipal Long userId, @PathVariable Long id){

        if(userId.equals(id)) throw new ForbiddenException(ForbiddenExceptionType.USER_UN_AUTHORIZED);

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
