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
public class SnsAPIServiceImpl implements SnsAPIService {

    @Value("${sns.kakao.userInfoUri}")
    private String KAKAO_INFO_URI;
    @Value("${sns.kakao.unlinkUri")
    private String KAKAO_UNLINK_URI;

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

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
        validateSnsUserDuplicate(userDTO.getEmail(), userDTO.getSnsId(), userType);
        //중복X -> region 세팅 및 회원가입 처리
        User user = userDTO.toEntity(
                userType, joinDTO.getUsername(), joinDTO.getPhoneNumber(),
                joinDTO.getDongCode(), joinDTO.getDongName());

        switch(userType){
            case KAKAO: user.setKakaoUser(userDTO.getSnsId()); break;//sns 유저 세팅
            default: throw new InvalidUserInputException(InvalidUserInputExceptionType.INVALID_USERTYPE);
        }
        return userRepository.save(user);
    }

    private void validateSnsUserDuplicate(String email, Long snsId, UserType userType){
        //sns 유저 중복 검증
        // email이 unique이기 때문에 이메일 부터 검증하고,
        // snsId와 userType 으로는 똑같은 sns계정이 있는지도 체크
        if(userRepository.existsByEmail(email))
            throw new InvalidUserInputException(InvalidUserInputExceptionType.ALREADY_EXISTS_SNS_USER);
        if(userRepository.existsBySnsIdAndUserType(snsId, userType))
            throw new InvalidUserInputException(InvalidUserInputExceptionType.ALREADY_EXISTS_SNS_USER);
    }

    @Override
    public void unlink(UserType userType, String accessToken) {
        String requestUrl = null;
        switch(userType){
            case KAKAO: requestUrl = KAKAO_UNLINK_URI; break;
            default: throw new InvalidUserInputException(InvalidUserInputExceptionType.INVALID_USERTYPE);
        }

        //헤더, 인코딩 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        //요청 보내기
        ResponseEntity<String> result = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
        log.info(result.getBody());
    }

}