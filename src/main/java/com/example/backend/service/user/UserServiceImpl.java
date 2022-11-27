package com.example.backend.service.user;

import com.example.backend.domain.Comment;
import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import com.example.backend.domain.post.Social;
import com.example.backend.dto.login.SocialJoinRequestDTO;
import com.example.backend.dto.user.SnsUserDTO;
import com.example.backend.dto.user.UserDefaultJoinRequestDTO;
import com.example.backend.dto.user.UserModifyRequestDTO;
import com.example.backend.global.exception.*;
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
    private final SnsUserService snsUserService;

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
    public User createSnsUser(UserType userType, SocialJoinRequestDTO joinDTO) {
        //카카오 사용자 정보 API에서 받아옴
        SnsUserDTO userDTO = snsUserService.getUserInfo(userType, joinDTO.getAccessToken());
        validateSocialUserDuplicate(userDTO.getEmail());
        //중복X -> region 세팅 및 회원가입 처리
        User user = userDTO.toEntity(
                userType, joinDTO.getUsername(), joinDTO.getPhoneNumber(),
                joinDTO.getDongCode(), joinDTO.getDongName());
        user.setKakaoUser(); //status 세팅
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User modify(String email, Long userId, UserModifyRequestDTO userDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        if(!user.getId().equals(userId)) throw new ForbiddenException(ForbiddenExceptionType.USER_UN_AUTHORIZED);

        //user 수정
        if(userDTO.getNickname() != null) user.updateNickname(userDTO.getNickname());
        if(userDTO.getBio() != null) user.updateBio(userDTO.getBio());
        if(userDTO.getProfile() != null) user.updateProfile(userDTO.getProfile());
        if(userDTO.getDongCode() != null && userDTO.getDongName() != null){
            user.updateRegion(userDTO.getDongCode(), userDTO.getDongName());
        }

        //TODO 만약 region 수정 가능하면 모임장인 social의 region도 수정되어야함(피드는 두는게 나은듯)

        return null;
    }

    //중복 검증
    private void validateDuplicate(String email, String nickName) {
        if(userRepository.existsByEmailAndNickname(email, nickName)){
            throw new InvalidUserInputException(InvalidUserInputExceptionType.ALREADY_EXIST_EMAIL_AND_NICKNAME);
        }
        else if(userRepository.existsByEmail(email)){
            throw new InvalidUserInputException(InvalidUserInputExceptionType.ALREADY_EXISTS_EMAIL);
        }
        else if(userRepository.existsByNickname(nickName)){
            throw new InvalidUserInputException(InvalidUserInputExceptionType.ALREADY_EXISTS_NICKNAME);
        }
    }

    private void validateSocialUserDuplicate(String email){
        if(userRepository.existsByEmail(email)){
            throw new InvalidUserInputException(InvalidUserInputExceptionType.ALREADY_EXISTS_KAKAO_USER);
        }
    }

    @Transactional
    @Override
    public void changeToWithdrawnUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

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
        Social social = socialRepository.findReadOnlyById(socialId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));
        User socialOwner = social.getUser(); //모임장
        //모임장인지 확인
        if(!email.equals(socialOwner.getEmail())) throw new ForbiddenException(ForbiddenExceptionType.USER_UN_AUTHORIZED);
        //강퇴하고 숫자-1
        socilaingRepository.deleteByUserIdAndSocialId(droppedUserId, socialId);
        social.minusCurrentNums();
    }


}
