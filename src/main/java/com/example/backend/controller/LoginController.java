package com.example.backend.controller;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.AuthDTO;
import com.example.backend.dto.response.ResponseDTO;
import com.example.backend.dto.login.DefaultLoginRequestDTO;
import com.example.backend.global.exception.user.UserInvalidInputException;
import com.example.backend.global.exception.user.UserInvalidInputExceptionType;
import com.example.backend.global.exception.UnAuthorizedException;
import com.example.backend.global.exception.UnAuthorizedExceptionType;
import com.example.backend.global.security.AuthToken;
import com.example.backend.global.security.AuthTokenProvider;
import com.example.backend.global.security.CookieProvider;
import com.example.backend.global.utils.ResponseUtils;
import com.example.backend.service.login.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "login/logout", description = "로그인 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final AuthTokenProvider authTokenProvider;
    private final CookieProvider cookieProvider;

    @Operation(summary = "기본 로그인", description = "AccessToken은 헤더로, RefreshToken은 쿠키로 반환합니다.")
    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public ResponseEntity<?> login(@RequestBody @Valid final DefaultLoginRequestDTO loginDTO){

        final User loginUser = loginService.defaultLogin(loginDTO);
        AuthToken AT = authTokenProvider.issueAccessToken(loginUser);
        AuthToken RT = authTokenProvider.issueRefreshToken(loginUser);
        loginService.updateRefresh(loginUser, RT);
        ResponseCookie RTcookie = cookieProvider.createRefreshTokenCookie(RT.getToken());
        return ResponseUtils.getLoginSuccessResponse(loginUser.getId(), AT, RTcookie,"로그인에 성공했습니다.");
    }

    @Operation(summary = "SNS 로그인", description = "카카오: userType=KAKAO, 처음 로그인하는 경우 errorCode -112가 반환되며, 'SNS 회원가입'으로 재요청하면 됩니다.")
    @PostMapping("/login/{userType}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<?> snsLogin(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("userType") String userTypeStr){

        UserType userType = UserType.valueOf(userTypeStr.toUpperCase());
        if(!userType.equals(UserType.KAKAO)) throw new UserInvalidInputException(UserInvalidInputExceptionType.INVALID_USERTYPE);

        final User loginUser = loginService.snsLogin(userType, accessToken);
        AuthToken AT = authTokenProvider.issueAccessToken(loginUser);
        AuthToken RT = authTokenProvider.issueRefreshToken(loginUser);
        loginService.updateRefresh(loginUser, RT);
        ResponseCookie RTcookie = cookieProvider.createRefreshTokenCookie(RT.getToken());
        return ResponseUtils.getLoginSuccessResponse(loginUser.getId(), AT, RTcookie, "카카오 로그인에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "새로고침 됐을때, accessToken이 만료됐을 때 호출", description = "RefreshToken 쿠키를 사용해서 AccessToken을 재발급합니다.")
    @PostMapping("/refresh")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken") String refreshTokenStr){
        AuthToken refreshToken = authTokenProvider.convertAuthToken(refreshTokenStr);
        //토큰 검증
        if(!refreshToken.validate()) throw new UnAuthorizedException(UnAuthorizedExceptionType.REFRESH_TOKEN_UN_AUTHORIZED);

        Long userId = refreshToken.getUserIdFromClaims();
        final AuthToken reissuedAT = loginService.refresh(userId, refreshToken);

        //set response
        AuthDTO authDTO = new AuthDTO(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, reissuedAT.getToken())
                .body(new ResponseDTO<>(authDTO, "AccessToken이 재발급되었습니다."));
    }
}
