package com.example.backend.controller;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.SocialDTO;
import com.example.backend.service.SocialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "social", description = "모임 게시글 API")
@RestController
@RequiredArgsConstructor
public class SocialController {

    private static SocialService socialService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모임 리스트 출력")
    @PostMapping("/socials")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    public ResponseEntity<ResponseDTO> getSocials(){
        List<SocialDTO> socialDTOList = socialService.getSocialList();
        return new ResponseEntity(ResponseDTO.builder().success(true).message("OK").data(socialDTOList).build(),HttpStatus.OK);
    }

}
