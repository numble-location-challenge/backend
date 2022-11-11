package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.login.KaKaoAuthRequestDTO;
import com.example.backend.dto.user.UserJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;
import com.example.backend.global.exception.InvalidInputException;
import com.example.backend.global.exception.InvalidInputExceptionType;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void createDefaultUser(UserJoinRequestDTO userJoinRequestDTO) {
        User user = userJoinRequestDTO.toEntity();

         if(validateDuplicate(user.getEmail(), user.getNickname())){
             //중복X -> 회원가입 처리
             user.encodePassword(passwordEncoder);
             userRepository.save(user);
         }
    }

    @Transactional
    @Override
    public void createKakaoUser(KaKaoAuthRequestDTO snsUserDTO) {
        User user = getKakaoUserInfo(snsUserDTO.getKakaoAccessToken());
        //TODO 세팅 및 회원가입 처리
    }

    @Override
    public User getKakaoUserInfo(String KakaoAccessToken) {
        //TODO 카카오 사용자 정보 API에서 [id, 이메일, 핸드폰 번호, 이름, 프사(?)] 를 받아옴

        return null;
    }

    //중복 검증
    private boolean validateDuplicate(String email, String nickName) {
        if(userRepository.existsByEmailAndNickname(email, nickName)){
            throw new InvalidInputException(InvalidInputExceptionType.ALREADY_EXIST_EMAIL_AND_NICKNAME);
        }
        else if(userRepository.existsByEmail(email)){
            throw new InvalidInputException(InvalidInputExceptionType.ALREADY_EXISTS_EMAIL);
        }
        else if(userRepository.existsByNickname(nickName)){
            throw new InvalidInputException(InvalidInputExceptionType.ALREADY_EXISTS_NICKNAME);
        }
        return true;
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User getUser(Long userId) {
        return null;
    }

    @Transactional
    @Override
    public User modifyUser(Long userId, UserModifyRequestDTO userModifyRequestDTO) {
        //null인지 체크할 것. null이면 값 안바꿈
        return null;
    }


}
