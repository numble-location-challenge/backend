package com.example.backend.controller;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.*;
import com.example.backend.dto.login.SnsJoinRequestDTO;
import com.example.backend.dto.user.UserDefaultJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;
import com.example.backend.dto.user.UserProfileDTO;
import com.example.backend.global.exception.InvalidUserInputException;
import com.example.backend.global.exception.InvalidUserInputExceptionType;
import com.example.backend.global.security.TokenService;
import com.example.backend.global.utils.ResponseUtils;
import com.example.backend.service.user.SnsUserService;
import com.example.backend.service.user.UserService;
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
import java.util.HashMap;

@Tag(name = "user", description = "회원 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SnsUserService snsUserService;
    private final TokenService tokenService;
    private final ResponseUtils responseUtils;

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원가입",
            description = "unique field 중복 시 errorCode -101(이메일), -102(닉네임), -103(이메일,닉네임)이 반환됩니다.")
    @PostMapping("/users")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    public ResponseEntity<?> join(@RequestBody @Valid final UserDefaultJoinRequestDTO userDefaultJoinRequestDTO, UriComponentsBuilder uriBuilder){
        final User createdUser = userService.createDefaultUser(userDefaultJoinRequestDTO);
        return ResponseUtils.getCreatedUserResponse(uriBuilder, createdUser.getId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "sns 회원가입", description = "카카오 userType=KAKAO, 이미 가입된 계정이라면 errorCode -122가 반환됩니다.")
    @PostMapping("/users/{userType}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<?> snsJoin(
            @PathVariable("userType") String userTypeStr,
            @RequestBody @Valid final SnsJoinRequestDTO joinDTO){

        UserType userType = UserType.valueOf(userTypeStr.toUpperCase());
        if(!userType.equals(UserType.KAKAO)) throw new InvalidUserInputException(InvalidUserInputExceptionType.INVALID_USERTYPE);

        final User createdUser = snsUserService.createSnsUser(userType, joinDTO);
        //회원가입 성공시 SNS 유저는 로그인에 성공한다
        HashMap<String, String> jwtMap = tokenService.getAccessAndRefreshToken(createdUser);
        return responseUtils.getLoginSuccessResponse(createdUser, jwtMap, userTypeStr + " 로그인에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/users/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> deleteUser(
            //TODO: id 검증 처리
            @AuthenticationPrincipal String email,
            @PathVariable Long id){
        userService.changeToWithdrawnUser(email);
        return new ResponseDTO<>(null, "정상 탈퇴되었습니다");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원 수정", description = "모든 변수 null available")
    @PutMapping("/users/{id}")
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

        final User modifiedUser = userService.modify(email, id, userModifyRequestDTO);
        UserProfileDTO userProfileDTO = new UserProfileDTO(modifiedUser);
        return new ResponseDTO<>(userProfileDTO, "정상 수정 처리 되었습니다.");
    }

    //내 프로필 조회할 때 피드, 댓글도 같이 들고 와야하는지? 일단 빼고 처리
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원 프로필 조회")
    @GetMapping("/users/{id}")
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
        UserProfileDTO userProfileDTO= new UserProfileDTO(user);
        return new ResponseDTO<>(userProfileDTO, "프로필 조회 결과");
    }
}
