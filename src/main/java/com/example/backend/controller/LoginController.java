package com.example.backend.controller;

import com.example.backend.domain.User;
import com.example.backend.dto.login.AuthDTO;
import com.example.backend.dto.login.KaKaoAuthRequestDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.login.DefaultLoginRequestDTO;
import com.example.backend.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Tag(name = "login/logout", description = "로그인 API")
@RestController
@RequiredArgsConstructor
public class LoginController {

    @Value("${jwt.access.header}")
    private String ACCESS_HEADER;

    @Value("${jwt.refresh.header}")
    private String REFRESH_HEADER;

    private final LoginService loginService;

    @Operation(summary = "기본 로그인")
    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
    })
    public ResponseEntity<?> login(@RequestBody final DefaultLoginRequestDTO loginDTO){

        User loginUser = loginService.defaultLogin(loginDTO.getEmail(),loginDTO.getPassword());
        HashMap<String, String> jwtMap = loginService.authorize(loginUser);
        return getLoginSuccessResponseEntity(loginUser, jwtMap);
    }

    //TODO 서비스 구현
    @Operation(summary = "카카오 로그인", description = "처음 로그인하는 경우 errorCode -112가 반환되며, /kakaojoin으로 재요청하면 됩니다.")
    @PostMapping("/kakaologin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<?> kakaologin(@RequestBody final KaKaoAuthRequestDTO authRequestDTO){

        User loginUser = loginService.kakaoLogin(authRequestDTO);
        HashMap<String, String> jwtMap = loginService.authorize(loginUser);
        return getLoginSuccessResponseEntity(loginUser, jwtMap);
    }

    private ResponseEntity<?> getLoginSuccessResponseEntity(User loginUser, HashMap<String, String> tokenMap) {
        //set data list
        List<AuthDTO> dataList = List.of(AuthDTO.builder()
                .userId(loginUser.getId())
                .email(loginUser.getEmail())
                .build());

        //set response
        ResponseDTO<AuthDTO> response = ResponseDTO.<AuthDTO>builder()
                .success(true).message("로그인에 성공했습니다.")
                .data(dataList)
                .build();

        return ResponseEntity.ok()
                .header(ACCESS_HEADER, tokenMap.get("AT"))
                .header(REFRESH_HEADER, tokenMap.get("RT"))
                .body(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> logout(
            @AuthenticationPrincipal String email,
            @RequestHeader(value = "Authorization") String accessToken,
            @RequestHeader(value = "Authorization-refresh") String refreshToken){
        loginService.logout(email, accessToken, refreshToken);
        return ResponseDTO.builder().success(true).message("로그아웃 되었습니다.").build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Access Token 재발급", description = "Authorization 및 Authorization-refresh 헤더가 요구됩니다.")
    @PostMapping("/refresh")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    public ResponseEntity<?> refresh(
            @RequestHeader(value = "Authorization") String accessToken,
            @RequestHeader(value = "Authorization-refresh") String refreshToken){

        User loginUser = loginService.getUserByRefreshToken(refreshToken);
        final String reissuedAccessToken = loginService.refresh(loginUser, accessToken, refreshToken);

        //set data list
        List<AuthDTO> dataList = List.of(AuthDTO.builder()
                .userId(loginUser.getId())
                .email(loginUser.getEmail())
                .build());

        //set response
        ResponseDTO<AuthDTO> response = ResponseDTO.<AuthDTO>builder()
                .success(true).message("Access Token이 재발급되었습니다.")
                .data(dataList)
                .build();

        return ResponseEntity.ok()
                .header(ACCESS_HEADER, reissuedAccessToken)
                .body(response);
    }
}
