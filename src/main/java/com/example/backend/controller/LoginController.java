package com.example.backend.controller;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.AuthDTO;
import com.example.backend.dto.login.SocialLoginRequestDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.login.DefaultLoginRequestDTO;
import com.example.backend.global.exception.InvalidUserInputException;
import com.example.backend.global.exception.InvalidUserInputExceptionType;
import com.example.backend.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Tag(name = "login/logout", description = "로그인 API")
@RestController
@RequiredArgsConstructor
public class LoginController {

    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_EXP;

    private final LoginService loginService;

    @Operation(summary = "기본 로그인", description = "AccessToken은 헤더로, RefreshToken은 쿠키로 반환합니다.")
    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public ResponseEntity<?> login(@RequestBody @Valid final DefaultLoginRequestDTO loginDTO){

        User loginUser = loginService.defaultLogin(loginDTO.getEmail(),loginDTO.getPassword());
        HashMap<String, String> jwtMap = loginService.getAccessAndRefreshToken(loginUser);
        return getTokenResponse(loginUser, jwtMap, "로그인에 성공했습니다.");
    }

    //TODO 서비스 구현
    @Operation(summary = "sns 로그인", description = "카카오: userType=KAKAO, 처음 로그인하는 경우 errorCode -112가 반환되며, region 설정 후 카카오 회원가입으로 재요청하면 됩니다.")
    @PostMapping("/login/sns/{userType}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<?> socialLogin(
            @PathVariable String userType,
            @RequestBody @Valid final SocialLoginRequestDTO authRequestDTO){

        if(!userType.equals(UserType.KAKAO)) throw new InvalidUserInputException(InvalidUserInputExceptionType.INVALID_USERTYPE);

        User loginUser = loginService.kakaoLogin(authRequestDTO);
        HashMap<String, String> jwtMap = loginService.getAccessAndRefreshToken(loginUser);
        return getTokenResponse(loginUser, jwtMap, "카카오 로그인에 성공했습니다.");
    }

    private ResponseEntity<?> getTokenResponse(User loginUser, HashMap<String, String> tokenMap, String message) {
        //set data list
        List<AuthDTO> dataList = List.of(AuthDTO.builder()
                .userId(loginUser.getId())
                .email(loginUser.getEmail())
                .build());

        //set response
        ResponseDTO<AuthDTO> response = ResponseDTO.<AuthDTO>builder()
                .success(true).message(message)
                .data(dataList)
                .build();

        //refresh token을 http only 쿠키에 담음
        ResponseCookie cookie = ResponseCookie.from("refreshToken",tokenMap.get("RT"))
                .httpOnly(true)
                .secure(false) //TODO SSL 인증서 필요해서 나중에
                .sameSite("None")
                .maxAge(REFRESH_EXP)
                .path("/refresh")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, tokenMap.get("AT"))
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    //TODO
//    @ResponseStatus(HttpStatus.OK)
//    @Operation(summary = "로그아웃", description = "수정 중입니다!!")
//    @PostMapping("/logout")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "OK"),
//            @ApiResponse(responseCode = "404", description = "NOT FOUND")
//    })
//    public ResponseDTO<?> logout(
//            @AuthenticationPrincipal String email,
//            @CookieValue(value = "refreshToken") String refreshToken){
//        loginService.logout(email, refreshToken);
//        return ResponseDTO.builder().success(true).message("로그아웃 되었습니다.").build();
//        return null;
//    }

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

        //set data list
        List<AuthDTO> dataList = List.of(AuthDTO.builder()
                .userId(loginUser.getId())
                .email(loginUser.getEmail())
                .build());

        //set response
        ResponseDTO<AuthDTO> response = ResponseDTO.<AuthDTO>builder()
                .success(true).message("AccessToken이 재발급되었습니다.")
                .data(dataList)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, reissuedAccessToken)
                .body(response);
    }
}
