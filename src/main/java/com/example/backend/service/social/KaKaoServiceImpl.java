package com.example.backend.service.social;

import com.example.backend.dto.user.KakaoUserDTO;
import com.example.backend.service.KakaoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

//TODO
@Service
public class KaKaoServiceImpl implements KakaoService {

    private final String requestUrl = "https://kapi.kakao.com/v2/user/me"; //카카오 유저 API

    @Value("${jwt.access.header}")
    private String ACCESS_HEADER;

    @Override
    public Long getUserId(String accessToken) {
        return null;
    }

    @Override
    public KakaoUserDTO getUserInfo(String accessToken) {
        //헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS_HEADER, "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //요청 보내기

        //사용자 정보를 JsonNode로 변경하여 필요한 데이터만 추출하여 Dto로 담아 반환
        return null;
    }
}
