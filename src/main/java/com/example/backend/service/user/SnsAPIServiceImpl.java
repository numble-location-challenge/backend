package com.example.backend.service.user;

import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.user.SnsUserDTO;
import com.example.backend.global.exception.UnAuthorizedException;
import com.example.backend.global.exception.UnAuthorizedExceptionType;
import com.example.backend.global.exception.user.UserInvalidInputException;
import com.example.backend.global.exception.user.UserInvalidInputExceptionType;
import com.example.backend.service.user.userInfo.KakaoUserInfo;
import com.example.backend.service.user.userInfo.UserInfo;
import com.example.backend.service.user.userInfo.UserInfoFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
@RequiredArgsConstructor
public class SnsAPIServiceImpl implements SnsAPIService {

    @Value("${sns.kakao.userInfoUri}")
    private String KAKAO_INFO_URI;
    @Value("${sns.kakao.unlinkUri")
    private String KAKAO_UNLINK_URI;

    private final RestTemplate restTemplate;

    @Override
    public Long getSnsId(UserType userType, String accessToken) {
        SnsUserDTO userInfo = getUserInfo(userType, accessToken);
        return userInfo.getSnsId();
    }

    @Override
    public SnsUserDTO getUserInfo(UserType userType, String accessToken) {
        log.info(KAKAO_INFO_URI);
        String requestUrl = null;
        switch(userType){
            case KAKAO: requestUrl = KAKAO_INFO_URI; break;
            default: throw new UserInvalidInputException(UserInvalidInputExceptionType.INVALID_USERTYPE);
        }

        //헤더, 인코딩 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        //요청 보내기
        ResponseEntity<String> result = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, String.class);
        log.info(result.getBody());

        //json parser
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfo userInfo = UserInfoFactory.getOAuth2UserInfo(userType);
        try{
            userInfo = objectMapper.readValue(result.getBody(), KakaoUserInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return UserInfoFactory.getUserDTOFromUserInfo(userType, userInfo);
    }

    @Override
    public Long unlink(UserType userType, String accessToken) {
        String requestUrl = null;
        switch(userType){
            case KAKAO: requestUrl = KAKAO_UNLINK_URI; break;
            default: throw new UserInvalidInputException(UserInvalidInputExceptionType.INVALID_USERTYPE);
        }

        //헤더, 인코딩 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        //요청 보내기
        ResponseEntity<String> result = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
        log.info(result.getBody());

        //json parser
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfo userInfo = UserInfoFactory.getOAuth2UserInfo(userType);
        try{
            userInfo = objectMapper.readValue(result.getBody(), KakaoUserInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        SnsUserDTO snsUserDTO = UserInfoFactory.getUserDTOFromUserInfo(userType, userInfo);
        if(snsUserDTO.getSnsId() == null) throw new UnAuthorizedException(UnAuthorizedExceptionType.API_REQUEST_FAIL);

        log.info("탈퇴된 sns회원의 snsId: {}", snsUserDTO.getSnsId());
        return snsUserDTO.getSnsId();
    }

}