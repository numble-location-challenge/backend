package com.example.backend.service.login;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.dto.login.DefaultLoginRequestDTO;
import com.example.backend.dto.login.SnsLoginRequestDTO;
import com.example.backend.global.exception.*;
import com.example.backend.global.security.JwtSubject;
import com.example.backend.global.security.TokenService;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.user.SnsUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginServiceImpl implements LoginService{

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SnsUserService snsUserService;

    @Override
    public User defaultLogin(DefaultLoginRequestDTO loginDTO) {
        //비밀번호 일치 확인
        User dbUser = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        if(passwordEncoder.matches(loginDTO.getPassword(), dbUser.getPassword())) return dbUser;
        else throw new InvalidUserInputException(InvalidUserInputExceptionType.ACCOUNT_NOT_MATCH);
    }
    
    @Override
    public User snsLogin(UserType userType, SnsLoginRequestDTO loginDTO) {
        //AT로 카카오 사용자 정보(id) 가져온다
        String email = snsUserService.getUserEmail(userType, loginDTO.getAccessToken());

        //1. DB에 있는 회원이면 컨트롤러로 돌아가 인가처리
        //2. 기존회원이 아니면 (region 필요) 에러코드
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_KAKAO_USER));
    }

    /**
     * 검증 & 만료체크를 거친다
     * @param refreshToken
     * @return User
     */
    @Override
    public User getUserByRefreshToken(String refreshToken) {
        //RT 만료되었다면 validate 메서드에서 ->로그인 유도 401
        JwtSubject jwtSubject;
        try{
            jwtSubject = tokenService.validateAndGetSubject(refreshToken);
        } catch(JwtException ex){
            throw new UnAuthorizedException(UnAuthorizedExceptionType.REFRESH_TOKEN_UN_AUTHORIZED);
        }
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

    @Transactional
    @Override
    public void updateRefresh(User loginUser, String refreshToken) {
        loginUser.updateRefreshToken(refreshToken); //DB의 RT 갈아끼우기
    }

}
