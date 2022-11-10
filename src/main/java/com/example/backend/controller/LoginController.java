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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@Tag(name = "login/logout", description = "로그인 API")
@RestController
@RequiredArgsConstructor
public class LoginController {

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

        return getLoginSuccessResponseEntity(loginUser);
    }

    //TODO 서비스 구현
    @Operation(summary = "카카오 로그인")
    @PostMapping("/kakaologin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<?> kakaologin(@RequestBody final KaKaoAuthRequestDTO authRequestDTO){

        User loginUser = loginService.kakaoLogin(authRequestDTO);

        if(loginUser == null){
            ResponseDTO<AuthDTO> response = ResponseDTO.<AuthDTO>builder()
                    .success(true).message("회원가입 되지 않은 카카오 계정입니다.")
                    .build();

            return ResponseEntity.ok().body(response);
        }
        return getLoginSuccessResponseEntity(loginUser);
    }

    private ResponseEntity<?> getLoginSuccessResponseEntity(User loginUser) {
        //set data list
        List<AuthDTO> dataList = List.of(new AuthDTO(loginUser.getId()));

        //set response
        ResponseDTO<AuthDTO> response = ResponseDTO.<AuthDTO>builder()
                .success(true).message("로그인에 성공했습니다.")
                .data(dataList)
                .build();

        HashMap<String, String> tokenMap = loginService.authorize(loginUser);

        return ResponseEntity.ok()
                .header("Authentication", tokenMap.get("AT"))
//                    .header("Authentication-refresh", tokenMap.get("RT"))
                .body(response);
    }

    //TODO 서비스 구현
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseDTO<?> logout(@AuthenticationPrincipal Long id){
        loginService.logout(id);
        return ResponseDTO.builder().success(true).message("로그아웃 되었습니다.").build();
    }
}
