package com.example.backend.controller;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.*;
import com.example.backend.dto.login.SocialJoinRequestDTO;
import com.example.backend.dto.user.UserDefaultJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;
import com.example.backend.dto.user.UserProfileDTO;
import com.example.backend.global.exception.InvalidUserInputException;
import com.example.backend.global.exception.InvalidUserInputExceptionType;
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
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
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
    public ResponseEntity<?> join(@RequestBody @Valid final UserDefaultJoinRequestDTO userDefaultJoinRequestDTO, UriComponentsBuilder uriBuilder){
        final User createdUser = userService.createDefaultUser(userDefaultJoinRequestDTO);
        return getCreatedUserResponse(uriBuilder, createdUser.getId());
    }

    //TODO 서비스 구현
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "sns 회원가입", description = "카카오: userType=KAKAO")
    @PostMapping("/join/sns/{userType}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<?> socialJoin(
            @PathVariable String userType,
            @RequestBody @Valid final SocialJoinRequestDTO joinDTO, UriComponentsBuilder uriBuilder){
        //프론트에서 회원정보 동의해주고 region, AT 가지고 회원가입 처리
        if(!userType.equals(UserType.KAKAO)) throw new InvalidUserInputException(InvalidUserInputExceptionType.INVALID_USERTYPE);
        final User createdUser = userService.createKakaoUser(joinDTO);
        return getCreatedUserResponse(uriBuilder, createdUser.getId());
    }

    private ResponseEntity<?> getCreatedUserResponse(UriComponentsBuilder uriBuilder, Long id) {
        URI location = uriBuilder.path("/user/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location)
                .body(new ResponseDTO<>(null, "회원가입 처리되었습니다."));
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/user/delete")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> deleteUser(@AuthenticationPrincipal String email){
        userService.delete(email);
        return new ResponseDTO<>(null, "정상 탈퇴되었습니다");
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
            @AuthenticationPrincipal String email,
            @PathVariable Long id,
            @RequestBody @Valid final UserModifyRequestDTO userModifyRequestDTO){

        //TODO id 검증.. principal 객체로 변경 후 고민
        final User user = userService.modify(email, userModifyRequestDTO);

        //set data list
        UserProfileDTO userProfileDTO = UserProfileDTO.builder()
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .region(user.getRegion())
                .bio(user.getBio())
                .build();

        return new ResponseDTO<>(userProfileDTO, "정상 수정 처리 되었습니다.");
    }

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
    public ResponseDTO<?> getUserInfo(
            @AuthenticationPrincipal String email,
            @PathVariable Long id){

        final User user = userService.getUser(email, id);

        //set data list
        List<UserProfileDTO> dataList = List.of(UserProfileDTO.builder()
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .region(user.getRegion())
                .bio(user.getBio())
                .build());

        return new ResponseDTO<>(dataList, "프로필 조회 결과");
    }
}
