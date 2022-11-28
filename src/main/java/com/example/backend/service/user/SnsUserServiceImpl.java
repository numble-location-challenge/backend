package com.example.backend.service.user;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.SnsJoinRequestDTO;
import com.example.backend.dto.user.SnsUserDTO;
import com.example.backend.global.exception.InvalidUserInputException;
import com.example.backend.global.exception.InvalidUserInputExceptionType;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.user.userInfo.KakaoUserInfo;
import com.example.backend.service.user.userInfo.UserInfo;
import com.example.backend.service.user.userInfo.UserInfoFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
@RequiredArgsConstructor
public class SnsUserServiceImpl implements SnsUserService {

    @Value("${sns.kakao.userInfoUri}")
    private String KAKAO_URL;

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    @Override
    public String getUserEmail(UserType userType, String accessToken) {
        SnsUserDTO userInfo = getUserInfo(userType, accessToken);
        return userInfo.getEmail();
    }

    @Override
    public SnsUserDTO getUserInfo(UserType userType, String accessToken) {
        log.info(KAKAO_URL);
        String requestUrl = null;
        switch(userType){
            case KAKAO: requestUrl = KAKAO_URL; break;
            default: throw new InvalidUserInputException(InvalidUserInputExceptionType.INVALID_USERTYPE);
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
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return UserInfoFactory.getUserDTOFromUserInfo(userType, userInfo);
    }

    @Transactional
    @Override
    public User createSnsUser(UserType userType, SnsJoinRequestDTO joinDTO) {
        //사용자 정보 API에서 받아옴
        SnsUserDTO userDTO = getUserInfo(userType, joinDTO.getAccessToken());
        validateSocialUserDuplicate(userDTO.getEmail());
        //중복X -> region 세팅 및 회원가입 처리
        User user = userDTO.toEntity(
                userType, joinDTO.getUsername(), joinDTO.getPhoneNumber(),
                joinDTO.getDongCode(), joinDTO.getDongName());
        user.setKakaoUser(); //status 세팅
        return userRepository.save(user);
    }

    private void validateSocialUserDuplicate(String email){
        if(userRepository.existsByEmail(email)){
            throw new InvalidUserInputException(InvalidUserInputExceptionType.ALREADY_EXISTS_KAKAO_USER);
        }
    }

}