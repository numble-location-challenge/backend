package com.example.backend.service.user;

import com.example.backend.controller.testCode.TestKakaoToken;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


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

    //인증코드로 token요청하기
    public TestKakaoToken requestToken(String code){

        String strUrl = "https://kauth.kakao.com/oauth/token"; //request를 보낼 주소
        TestKakaoToken kakaoToken = new TestKakaoToken(); //response를 받을 객체

        try{
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //url Http 연결 생성

            //POST 요청        conn.setRequestMethod("POST");
            conn.setDoOutput(true);//outputStreamm으로 post 데이터를 넘김

            //파라미터 세팅
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            //0번 파라미터 grant_type 입니다 authorization_code로 고정이라니 고정등록해줍니다
            sb.append("grant_type=authorization_code");

            //1번 파라미터 client_id입니다. ***자신의 앱 REST API KEY로 변경해주세요***
            sb.append("&client_id=2ded0b5666f72f5ea5c093b1ff80e81c");

            //2번 파라미터 redirect_uri입니다. ***자신의 redirect uri로 변경해주세요***
            sb.append("&redirect_uri=http://localhost:8080/oauth2/test/code");

            //3번 파라미터 code입니다. 인자로 받아온 인증코드입니다.
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();//실제 요청을 보내는 부분

            //실제 요청을 보내는 부분, 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responsecode(200이면성공): {}",responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            log.info("response body: {}",result);


            //Jackson으로 json 파싱할 것임
            ObjectMapper mapper = new ObjectMapper();
            //kakaoToken에 result를 KakaoToken.class 형식으로 변환하여 저장
            kakaoToken = mapper.readValue(result, TestKakaoToken.class);

            //api호출용 access token
            String access_Token = kakaoToken.getAccess_token();
            //access 토큰 만료되면 refresh token사용(유효기간 더 김)
            String refresh_Token=kakaoToken.getRefresh_token();

            log.info("access_token = {}",access_Token);
            log.info("refresh_token = {}", refresh_Token);

            br.close();
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        log.info("카카오토큰생성완료>>>{}",kakaoToken);
        return kakaoToken;
    }

}