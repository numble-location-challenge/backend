package com.example.backend.service.login;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.DefaultLoginRequestDTO;
import com.example.backend.dto.login.SnsLoginRequestDTO;
import com.example.backend.global.exception.*;
import com.example.backend.global.exception.user.UserInvalidInputException;
import com.example.backend.global.exception.user.UserInvalidInputExceptionType;
import com.example.backend.global.security.AuthToken;
import com.example.backend.global.security.AuthTokenProvider;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.user.SnsAPIService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LoginServiceImpl implements LoginService{

    private final AuthTokenProvider authTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SnsAPIService snsAPIService;

    @Override
    public User defaultLogin(DefaultLoginRequestDTO loginDTO) {
        //비밀번호 일치 확인
        User dbUser = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        if(passwordEncoder.matches(loginDTO.getPassword(), dbUser.getPassword())) return dbUser;
        else throw new UserInvalidInputException(UserInvalidInputExceptionType.ACCOUNT_NOT_MATCH);
    }
    
    @Override
    public User snsLogin(UserType userType, SnsLoginRequestDTO loginDTO) {
        //AT로 사용자 정보(sns API의 id) 가져옴
        Long snsId = snsAPIService.getSnsId(userType, loginDTO.getAccessToken());
        log.info("sns API user id: {}",snsId.toString());
        //1. DB에 있는 회원이면 컨트롤러로 돌아가 인가처리
        //2. 기존회원이 아니면 회원가입 유도
        return userRepository.findBySnsIdAndUserType(snsId, userType)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_KAKAO_USER));
    }

    //RT 검증 실패시 401 에러 발생
    @Override
    public AuthToken refresh(Long userId, AuthToken refreshToken) {
        //DB에 있는 RT랑 비교
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        if(!refreshToken.getToken().equals(user.getRefreshToken())) throw new UnAuthorizedException(UnAuthorizedExceptionType.REFRESH_TOKEN_UN_AUTHORIZED);
        //RT가 유효하므로 AT 재발급
        return authTokenProvider.issueAccessToken(user);
    }

    @Transactional
    @Override
    public void updateRefresh(User loginUser, AuthToken refreshToken) {
        loginUser.updateRefreshToken(refreshToken.getToken()); //DB의 RT 갈아끼우기
    }

}
