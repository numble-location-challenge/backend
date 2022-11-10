package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.KaKaoAuthRequestDTO;
import com.example.backend.global.exception.EntityNotExistsException;
import com.example.backend.global.exception.EntityNotExistsExceptionType;
import com.example.backend.global.exception.InvalidInputException;
import com.example.backend.global.exception.InvalidInputExceptionType;
import com.example.backend.global.security.TokenService;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginServiceImpl implements LoginService{

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public User defaultLogin(String email, String password) {
        //비밀번호 일치 확인
        User dbUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_MEMBER));

        if(passwordEncoder.matches(password, dbUser.getPassword())) return dbUser;
        else throw new InvalidInputException(InvalidInputExceptionType.ACCOUNT_NOT_MATCH);
    }

    //TODO 프론트랑 협의 필요
    @Override
    public User kakaoLogin(KaKaoAuthRequestDTO loginDTO) {
        getKakaoUserInfo(loginDTO.getKakaoAccessToken());

        //가져온 값으로 DB id, type 검증
        if(true){ //1. DB에 있는 회원이면 로그인 처리 후 토큰 발급
            User testUser = User.builder().userType(UserType.KAKAO).build();
            return testUser;
        }
        //2. DB에 없으면 null
        else return null;
    }

    @Override
    public User getKakaoUserInfo(String KakaoAccessToken) {
        //TODO AT로 필요한 정보(id,이메일 정도?) 가져옴
        return null;
    }

    @Override
    public HashMap<String,String> authorize(User user) {
        //토큰 2개 생성
        final String AccessToken = tokenService.create(user); //AT 생성
        final String RefreshToken = "sdfsdf"; //TODO RT 생성, 두개 한꺼번에 얻어오는게 낫나 음

        HashMap<String,String> token = new HashMap<>();
        token.put("AT", AccessToken);
        token.put("RT", RefreshToken);

        return token;
    }

    @Override
    public void logout(Long userId) {

    }

}