package com.example.backend.service.user;

import com.example.backend.domain.Comment;
import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;
import com.example.backend.domain.enumType.SocialStatus;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.domain.post.Social;
import com.example.backend.dto.login.SnsJoinRequestDTO;
import com.example.backend.dto.user.SnsUserDTO;
import com.example.backend.dto.user.UserDefaultJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;
import com.example.backend.global.exception.*;
import com.example.backend.global.exception.social.SocialInvalidInputException;
import com.example.backend.global.exception.social.SocialInvalidInputExceptionType;
import com.example.backend.global.exception.user.UserInvalidInputException;
import com.example.backend.global.exception.user.UserInvalidInputExceptionType;
import com.example.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SocialRepository socialRepository;
    private final SocialingRepository socilaingRepository;
    private final CommentRepository commentRepository;
    private final SnsAPIService snsAPIService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User createDefaultUser(UserDefaultJoinRequestDTO joinDTO) {
        validateDuplicate(joinDTO.getEmail(), joinDTO.getNickname());

        //중복X -> 회원가입 처리
        User user = joinDTO.toEntity();
        user.encodePassword(passwordEncoder);
        user.setDefaultUser();
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User createSnsUser(UserType userType, SnsJoinRequestDTO joinDTO) {
        //사용자 정보 API에서 받아옴
        SnsUserDTO userDTO = snsAPIService.getUserInfo(userType, joinDTO.getAccessToken());
        validateSnsUserDuplicate(userDTO.getEmail(), userDTO.getSnsId(), userType);
        //중복X -> region 세팅 및 회원가입 처리
        User user = userDTO.toEntity(
                userType, joinDTO.getUsername(), joinDTO.getPhoneNumber(),
                joinDTO.getDongCode(), joinDTO.getDongName());

        switch(userType){
            case KAKAO: user.setKakaoUser(userDTO.getSnsId()); break;//sns 유저 세팅
            default: throw new UserInvalidInputException(UserInvalidInputExceptionType.INVALID_USERTYPE);
        }
        return userRepository.save(user);
    }

    private void validateSnsUserDuplicate(String email, Long snsId, UserType userType){
        //sns 유저 중복 검증
        // email이 unique이기 때문에 이메일 부터 검증하고,
        // snsId와 userType 으로는 똑같은 sns계정이 있는지도 체크
        if(userRepository.existsByEmail(email))
            throw new UserInvalidInputException(UserInvalidInputExceptionType.ALREADY_EXISTS_SNS_USER);
        if(userRepository.existsBySnsIdAndUserType(snsId, userType))
            throw new UserInvalidInputException(UserInvalidInputExceptionType.ALREADY_EXISTS_SNS_USER);
    }

    @Transactional
    @Override
    public User modify(Long userId, UserModifyRequestDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        //user 수정
        if(userDTO.getNickname() != null) user.updateNickname(userDTO.getNickname());
        if(userDTO.getBio() != null) user.updateBio(userDTO.getBio());
        if(userDTO.getProfile() != null) user.updateProfile(userDTO.getProfile());
        if(userDTO.getDongCode() != null && userDTO.getDongName() != null){
            user.updateRegion(userDTO.getDongCode(), userDTO.getDongName());
        }

        return user;
    }

    //중복 검증
    private void validateDuplicate(String email, String nickName) {
        if(userRepository.existsByEmailAndNickname(email, nickName)){
            throw new UserInvalidInputException(UserInvalidInputExceptionType.ALREADY_EXIST_EMAIL_AND_NICKNAME);
        }
        else if(userRepository.existsByEmail(email)){
            throw new UserInvalidInputException(UserInvalidInputExceptionType.ALREADY_EXISTS_EMAIL);
        }
        else if(userRepository.existsByNickname(nickName)){
            throw new UserInvalidInputException(UserInvalidInputExceptionType.ALREADY_EXISTS_NICKNAME);
        }
    }

    @Transactional
    @Override
    public void changeToWithdrawnUser(User user) {
        //DB 에서 삭제하지 않고 상태만 변경
        user.setWithdrawStatus();
        //연결된 소셜링 삭제(참여한 모임)
        socilaingRepository.deleteAllByUserId(user.getId());
        //내가 모임장인 소셜 삭제
        socialRepository.deleteAllByUserId(user.getId());

        //작성한 댓글 deleted true로 변경 TODO: test 필요
        List<Comment> commentList = commentRepository.findAllByUserId(user.getId());
        if(commentList != null){
            commentList.forEach(Comment::setDeleted);
        }
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
    }

    @Override
    public User getUserBySnsId(Long snsId) {
        return userRepository.findBySnsId(snsId).orElse(null);
    }

    @Transactional
    @Override
    public void participateSocial(Long userId, Long socialId) {
        //엔티티 조회
        User findUser = userRepository.findReadOnlyById(userId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        Social findSocial = socialRepository.findById(socialId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));

        //모임이 꽉찬 상태면 x
        if(findSocial.getStatus() == SocialStatus.FULL) throw new SocialInvalidInputException(SocialInvalidInputExceptionType.FULL_STATUS);
        //중복 신청이면 x
        if(findSocial.getUser().getId().equals(findUser.getId())) throw new SocialInvalidInputException(SocialInvalidInputExceptionType.ALREADY_APPLIED);

        //신청 처리
        findSocial.addCurrentNums(); // 참여인원 +1
        Socialing socialing = Socialing.createSocialing(findUser);
        findSocial.addSocialing(socialing);
        socilaingRepository.save(socialing);
    }

    @Transactional
    @Override
    public void cancelSocialParticipation(Long userId, Long socialId) {
        User findUser = userRepository.findReadOnlyById(userId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        Social findSocial = socialRepository.findById(socialId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));
        //모임 취소처리
        findSocial.minusCurrentNums(); //참여인원-1
        socilaingRepository.deleteByUserIdAndSocialId(findUser.getId(), socialId);
    }

    @Transactional
    @Override
    public void kickOutUserFromSocial(Long userId, Long socialId, Long droppedUserId) {
        //엔티티 조회
        Social social = socialRepository.findById(socialId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));
        User socialOwner = social.getUser(); //모임장
        //모임장인지 확인
        if(!userId.equals(socialOwner.getId())) throw new ForbiddenException(ForbiddenExceptionType.USER_UN_AUTHORIZED);
        //강퇴 처리
        socilaingRepository.deleteByUserIdAndSocialId(droppedUserId, socialId);
        social.minusCurrentNums();//강퇴하고 참여인원-1
    }

}
