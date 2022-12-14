package com.example.backend.controller;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.user.SnsJoinRequestDTO;
import com.example.backend.dto.response.ResponseDTO;
import com.example.backend.dto.user.UserDefaultJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;
import com.example.backend.dto.user.UserProfileDTO;
import com.example.backend.global.exception.ForbiddenException;
import com.example.backend.global.exception.ForbiddenExceptionType;
import com.example.backend.global.exception.user.UserInvalidInputException;
import com.example.backend.global.exception.user.UserInvalidInputExceptionType;
import com.example.backend.global.security.jwt.authToken.AuthToken;
import com.example.backend.global.security.CustomUserDetails;
import com.example.backend.global.security.jwt.authToken.AuthTokenProvider;
import com.example.backend.global.security.CookieProvider;
import com.example.backend.global.utils.ResponseUtils;
import com.example.backend.service.user.SnsAPIService;
import com.example.backend.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@Tag(name = "user", description = "회원 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SnsAPIService snsAPIService;
    private final AuthTokenProvider authTokenProvider;
    private final CookieProvider cookieProvider;

    @Operation(summary = "회원가입",
            description = "unique field 중복 시 errorCode -101(이메일), -102(닉네임), -103(이메일,닉네임)이 반환됩니다. bio만 null availabe 합니다.")
    @PostMapping("/users")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    public ResponseEntity<?> join(@RequestBody @Valid final UserDefaultJoinRequestDTO userDefaultJoinRequestDTO, UriComponentsBuilder uriBuilder){
        final User createdUser = userService.createDefaultUser(userDefaultJoinRequestDTO);
        return ResponseUtils.getCreatedUserResponse(uriBuilder, createdUser.getId());
    }

    @Operation(summary = "SNS 회원가입", description = "카카오 userType=KAKAO, 이미 가입된 계정이라면 errorCode -122가 반환됩니다. 회원가입이 완료된 후 자동 로그인 처리됩니다.")
    @PostMapping("/users/{userType}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<?> snsJoin(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("userType") String userTypeStr,
            @RequestBody @Valid final SnsJoinRequestDTO joinDTO){

        UserType userType = UserType.valueOf(userTypeStr.toUpperCase());

        final User createdUser = userService.createSnsUser(userType, accessToken, joinDTO);
        //회원가입 성공시 SNS 유저는 로그인에 성공한다
        AuthToken AT = authTokenProvider.issueAccessToken(createdUser);
        AuthToken RT = authTokenProvider.issueRefreshToken(createdUser);
        ResponseCookie RTcookie = cookieProvider.createRefreshTokenCookie(RT.getToken());
        return ResponseUtils.getLoginSuccessResponse(createdUser.getId(), AT, RTcookie, userTypeStr + " 로그인에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원 탈퇴", description = "카카오 계정인 경우 errorCode -123가 반환됩니다.")
    @DeleteMapping("/users/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> deleteUser(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id){

        checkPathResource(user.getUserId(), id);

        if(user.getUserType() == UserType.KAKAO){
            throw new UserInvalidInputException(UserInvalidInputExceptionType.CANT_DELETE_KAKAO_USER);
        }

        User findUser = userService.getUserById(id);
        userService.changeToWithdrawnUser(findUser);
        return new ResponseDTO<>(null, "정상 탈퇴되었습니다");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "SNS 회원 탈퇴")
    @DeleteMapping("/users/sns/{userType}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "403", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> deleteSnsUser(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable(value = "userType") String userTypeStr,
            @AuthenticationPrincipal CustomUserDetails user){

        UserType userType = UserType.valueOf(userTypeStr.toUpperCase());
        Long disConnectedId = snsAPIService.unlink(userType, accessToken);
        User findUser = userService.getUserBySnsId(disConnectedId);
        userService.changeToWithdrawnUser(findUser);
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
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id,
            @RequestBody @Valid final UserModifyRequestDTO userModifyRequestDTO){

        checkPathResource(user.getUserId(), id);
        final User modifiedUser = userService.modify(user.getUserId(), userModifyRequestDTO);
        UserProfileDTO userProfileDTO = new UserProfileDTO(modifiedUser);
        return new ResponseDTO<>(userProfileDTO, "정상 수정 처리 되었습니다.");
    }

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
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id){

        checkPathResource(user.getUserId(), id);
        final User findUser = userService.getUserById(user.getUserId());
        UserProfileDTO userProfileDTO= new UserProfileDTO(findUser);
        return new ResponseDTO<>(userProfileDTO, "프로필 조회 결과");
    }

    public void checkPathResource(Long authId, Long pathId){
        if(!authId.equals(pathId)) throw new ForbiddenException(ForbiddenExceptionType.USER_UN_AUTHORIZED);
    }
}
