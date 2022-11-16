package com.example.backend.service;

import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Social;
import com.example.backend.dto.login.KaKaoAuthRequestDTO;
import com.example.backend.dto.user.UserJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;
import com.example.backend.global.exception.*;
import com.example.backend.repository.SocialRepository;
import com.example.backend.repository.SocialingRepository;
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
    private final SocialRepository socialRepository;
    private final SocialingRepository socilaingRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User createDefaultUser(UserJoinRequestDTO userJoinRequestDTO) {
        User user = userJoinRequestDTO.toEntity();

        validateDuplicate(user.getEmail(), user.getNickname());
        //중복X -> 회원가입 처리
        user.encodePassword(passwordEncoder);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User createKakaoUser(KaKaoAuthRequestDTO kaKaoAuthRequestDTO) {
        User user = getKakaoUserInfo(kaKaoAuthRequestDTO.getKakaoAccessToken());
        return null;
        //TODO 세팅 및 회원가입 처리
    }

    @Override
    public User getKakaoUserInfo(String KakaoAccessToken) {
        //TODO 카카오 사용자 정보 API에서 [id, 이메일, 핸드폰 번호, 이름, 프사(?)] 를 받아옴

        return null;
    }

    @Override
    public User modify(UserModifyRequestDTO userModifyRequestDTO, String email) {
        //TODO
        return null;
    }

    //중복 검증
    private void validateDuplicate(String email, String nickName) {
        if(userRepository.existsByEmailAndNickname(email, nickName)){
            throw new InvalidInputException(InvalidInputExceptionType.ALREADY_EXIST_EMAIL_AND_NICKNAME);
        }
        else if(userRepository.existsByEmail(email)){
            throw new InvalidInputException(InvalidInputExceptionType.ALREADY_EXISTS_EMAIL);
        }
        else if(userRepository.existsByNickname(nickName)){
            throw new InvalidInputException(InvalidInputExceptionType.ALREADY_EXISTS_NICKNAME);
        }
    }

    @Transactional
    @Override
    public void delete(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    public User getUser(String email, Long id) {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        if(findUser.getId().equals(id)) return findUser;
        else throw new ForbiddenException(ForbiddenExceptionType.USER_UN_AUTHORIZED);
    }

    //모임신청
    @Transactional
    @Override
    public void participateSocial(String email, Long socialId) {
        //TODO
    }

    //모임 취소
    @Transactional
    @Override
    public void cancelSocialParticipation(String email, Long socialId) {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        //user pk 랑 socialId로 소셜링 찾아옴
        //TODO

    }

    //모임 강퇴
    @Transactional
    @Override
    public void kickOutUserFromSocial(String email, Long socialId, Long droppedUserId) {
        //TODO
    }


}
