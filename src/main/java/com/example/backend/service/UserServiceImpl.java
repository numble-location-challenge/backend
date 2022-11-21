package com.example.backend.service;

import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;
import com.example.backend.domain.enumType.SocialStatus;
import com.example.backend.domain.post.Post;
import com.example.backend.domain.post.Social;
import com.example.backend.dto.login.SocialJoinRequestDTO;
import com.example.backend.dto.user.KakaoUserDTO;
import com.example.backend.dto.user.UserDefaultJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;
import com.example.backend.global.exception.*;
import com.example.backend.repository.PostRepository;
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
    private final PostRepository postRepository;
    private final KakaoService kakaoService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User createDefaultUser(UserDefaultJoinRequestDTO userDefaultJoinRequestDTO) {
        User user = userDefaultJoinRequestDTO.toEntity();

        validateDuplicate(user.getEmail(), user.getNickname());
        //중복X -> 회원가입 처리
        user.encodePassword(passwordEncoder);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User createKakaoUser(SocialJoinRequestDTO joinDTO) {
        //카카오 사용자 정보 API에서 받아옴
        KakaoUserDTO userDTO = kakaoService.getUserInfo(joinDTO.getAccessToken());

        validateSocialUserDuplicate(userDTO.getId(), userDTO.getEmail());
        //중복X -> region 세팅 및 회원가입 처리
        User user = userDTO.toEntity(joinDTO.getRegion());
        return userRepository.save(user);
    }

    @Override
    public User modify(String email, UserModifyRequestDTO userModifyRequestDTO) {
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

    private void validateSocialUserDuplicate(Long id, String email){
        boolean findUser = userRepository.existsById(id);
        if(findUser) throw new InvalidInputException(InvalidInputExceptionType.ALREADY_EXISTS_KAKAO_USER);
        else if(userRepository.existsByEmail(email)){
            throw new InvalidInputException(InvalidInputExceptionType.ALREADY_EXISTS_EMAIL);
        }
    }

    @Transactional
    @Override
    public void delete(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    public User getUser(String email, Long id) {
        //url로 들어온 id와 principal의 유저가 같은지 확인
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        if(findUser.getId().equals(id)) return findUser;
        else throw new ForbiddenException(ForbiddenExceptionType.USER_UN_AUTHORIZED);
    }

    //모임신청
    @Transactional
    @Override
    public void participateSocial(String email, Long socialId) {
        //이미 신청된 유저면 신청안되게 하는건 프론트에서 거르는 거겠지..?
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        Social findSocial = socialRepository.findById(socialId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));

        //모임이 꽉찬 상태면 x
//        if(findSocial.getStatus() != SocialStatus.AVAILABLE) throw new ex
        //중복 신청이면 x

        Socialing socialing = Socialing.createSocialing(findUser);
        findSocial.addSocialing(socialing);

        socilaingRepository.save(socialing);
    }

    //모임 취소
    @Transactional
    @Override
    public void cancelSocialParticipation(String email, Long socialId) {
        User findUser = userRepository.findReadOnlyByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        socilaingRepository.deleteByUserIdAndSocialId(findUser.getId(), socialId);
    }

    //모임 강퇴
    @Transactional
    @Override
    public void kickOutUserFromSocial(String email, Long socialId, Long droppedUserId) {
        //엔티티 조회
        Post post = postRepository.findReadOnlyById(socialId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_POST));

        Social social = socialRepository.findReadOnlyById(socialId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));
        User socialOwner = post.getUser(); //모임장
        //모임장인지 확인
        if(!email.equals(socialOwner.getEmail())) throw new ForbiddenException(ForbiddenExceptionType.USER_UN_AUTHORIZED);
        //강퇴하고 숫자-1
        socilaingRepository.deleteByUserIdAndSocialId(droppedUserId, socialId);
        social.minusCurrentNums();
    }


}
