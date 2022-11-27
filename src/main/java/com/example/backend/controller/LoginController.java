package com.example.backend.controller;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.AuthDTO;
import com.example.backend.dto.login.SocialLoginRequestDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.login.DefaultLoginRequestDTO;
import com.example.backend.global.exception.InvalidUserInputException;
import com.example.backend.global.exception.InvalidUserInputExceptionType;
import com.example.backend.global.security.TokenService;
import com.example.backend.global.utils.ResponseUtils;
import com.example.backend.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@Tag(name = "login/logout", description = "로그인 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final TokenService tokenService;
    private final ResponseUtils responseUtils;

    @Operation(summary = "기본 로그인", description = "AccessToken은 헤더로, RefreshToken은 쿠키로 반환합니다.")
    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public ResponseEntity<?> login(@RequestBody @Valid final DefaultLoginRequestDTO loginDTO){

        final User loginUser = loginService.defaultLogin(loginDTO);
        HashMap<String, String> jwtMap = tokenService.getAccessAndRefreshToken(loginUser);
        loginService.updateRefresh(loginUser, jwtMap.get("RT"));
        return responseUtils.getLoginSuccessResponse(loginUser, jwtMap, "로그인에 성공했습니다.");
    }

    @Operation(summary = "sns 로그인", description = "카카오: userType=KAKAO, 처음 로그인하는 경우 errorCode -112가 반환되며, region 설정 후 카카오 회원가입으로 재요청하면 됩니다.")
    @PostMapping("/login/sns/{userType}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<?> socialLogin(
            @PathVariable("userType") String userTypeStr,
            @RequestBody @Valid final SocialLoginRequestDTO authRequestDTO){

        UserType userType = UserType.valueOf(userTypeStr.toUpperCase());
        if(!userType.equals(UserType.KAKAO)) throw new InvalidUserInputException(InvalidUserInputExceptionType.INVALID_USERTYPE);

        final User loginUser = loginService.snsLogin(userType, authRequestDTO);
        HashMap<String, String> jwtMap = tokenService.getAccessAndRefreshToken(loginUser);
        loginService.updateRefresh(loginUser, jwtMap.get("RT"));
        return responseUtils.getLoginSuccessResponse(loginUser, jwtMap, "카카오 로그인에 성공했습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "새로고침 됐을때, accessToken이 만료됐을 때 호출", description = "RefreshToken 쿠키를 사용해서 AccessToken을 재발급합니다.")
    @PostMapping("/refresh")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken") String refreshToken){

        User loginUser = loginService.getUserByRefreshToken(refreshToken);
        final String reissuedAccessToken = loginService.refresh(loginUser, refreshToken);

        //set response
        AuthDTO authDTO = new AuthDTO(loginUser);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, reissuedAccessToken)
                .body(new ResponseDTO<>(authDTO, "AccessToken이 재발급되었습니다."));
    }
}
