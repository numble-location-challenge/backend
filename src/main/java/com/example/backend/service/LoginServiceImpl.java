package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.login.SocialLoginRequestDTO;
import com.example.backend.global.exception.*;
import com.example.backend.global.security.JwtSubject;
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
    private final KakaoService kakaoService;

    @Override
    public User defaultLogin(String email, String password) {
        //비밀번호 일치 확인
        User dbUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        if(passwordEncoder.matches(password, dbUser.getPassword())) return dbUser;
        else throw new InvalidUserInputException(InvalidUserInputExceptionType.ACCOUNT_NOT_MATCH);
    }

    //TODO 프론트랑 협의 필요
    @Override
    public User kakaoLogin(SocialLoginRequestDTO loginDTO) {
        //AT로 카카오 사용자 정보(id) 가져온다
        Long kakaoId = kakaoService.getUserId(loginDTO.getAccessToken());

        //가져온 값 중 '카카오의 회원번호'로 DB에 있는지 찾는다
        //1. DB에 있는 회원이면 컨트롤러로 돌아가 인가처리
        //2. 기존회원이 아니면 (region 필요) 에러코드
        return userRepository.findById(kakaoId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_KAKAO_USER));
    }

    @Transactional
    @Override
    public HashMap<String,String> getAccessAndRefreshToken(User user) {
        //토큰 2개 생성
        final String accessToken = tokenService.issueAccessToken(user); //AT 생성
        final String refreshToken = tokenService.issueRefreshToken(user); //RT 생성

        user.updateRefreshToken(refreshToken); //DB의 RT 갈아끼우기

        HashMap<String,String> token = new HashMap<>();
        token.put("AT", accessToken);
        token.put("RT", refreshToken);

        return token;
    }

    @Override
    public void logout(String email, String refreshToken) {
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        findUser.deleteRefreshToken(); //DB의 RT 삭제
    }

    /**
     * 검증 & 만료체크를 거친다
     * @param refreshToken
     * @return User
     */
    @Override
    public User getUserByRefreshToken(String refreshToken) {
        //RT 만료되었다면 validate 메서드에서 ->로그인 유도 401
        JwtSubject jwtSubject = tokenService.validateAndGetSubject(refreshToken);
        //토큰 subject email로 User 가져옴
        return userRepository.findByEmail(jwtSubject.getEmail())
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
    }

    /**
     * RT 검증 실패시 401 에러 발생
     * @param user
     * @param refreshToken
     * @return AccessToken
     */
    @Override
    public String refresh(User user, String refreshToken) {
        //DB에 있는 RT랑 비교
        if(!refreshToken.equals(user.getRefreshToken())) throw new UnAuthorizedException(UnAuthorizedExceptionType.REFRESH_TOKEN_UN_AUTHORIZED);
        //RT가 유효하므로 AT 재발급
        return tokenService.issueAccessToken(user);
    }

}
