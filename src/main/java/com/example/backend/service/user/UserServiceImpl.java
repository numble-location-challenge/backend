package com.example.backend.service.user;

import com.example.backend.domain.Comment;
import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Social;
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
        //TODO 만약 region 수정 가능하면 모임장인 social의 region도 수정되어야함(피드는 두는게 나은듯)
        return user;
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

    @Transactional
    @Override
    public void changeToWithdrawnUser(Long userId) {
        User user = userRepository.findById(userId)
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
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
    }

    @Transactional
    @Override
    public void participateSocial(Long userId, Long socialId) {
        //이미 신청된 유저면 신청안되게 하는건 프론트에서 거르는 거겠지..?
        User findUser = userRepository.findReadOnlyById(userId)
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

    @Transactional
    @Override
    public void cancelSocialParticipation(Long userId, Long socialId) {
        User findUser = userRepository.findReadOnlyById(userId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        socilaingRepository.deleteByUserIdAndSocialId(findUser.getId(), socialId);
    }

    @Transactional
    @Override
    public void kickOutUserFromSocial(Long userId, Long socialId, Long droppedUserId) {
        //엔티티 조회
        Social social = socialRepository.findReadOnlyById(socialId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));
        User socialOwner = social.getUser(); //모임장
        //모임장인지 확인
        if(!userId.equals(socialOwner.getId())) throw new ForbiddenException(ForbiddenExceptionType.USER_UN_AUTHORIZED);
        //강퇴하고 숫자-1
        socilaingRepository.deleteByUserIdAndSocialId(droppedUserId, socialId);
        social.minusCurrentNums();
    }


}
