package com.example.backend.service.social;

import com.example.backend.domain.Like;
import com.example.backend.domain.PostImage;
import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;

import com.example.backend.domain.enumType.SocialStatus;
import com.example.backend.domain.post.Social;
import com.example.backend.domain.tag.Category;
import com.example.backend.domain.tag.SocialTag;
import com.example.backend.domain.tag.Tag;
import com.example.backend.dto.PostImageDTO;
import com.example.backend.dto.TagDTO;
import com.example.backend.dto.social.*;
import com.example.backend.domain.tag.SocialTag;
import com.example.backend.global.exception.EntityNotExistsException;
import com.example.backend.global.exception.EntityNotExistsExceptionType;
import com.example.backend.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
@EnableScheduling
public class SocialServiceImpl implements SocialService {

    private final UserRepository userRepository;
    private final SocialRepository socialRepository;
    private final TagRepository tagRePository;
    private final PostImageRepository postImageRepository;

    private final LikesRepository likesRepository;

    //모임 게시글 생성
    @Transactional
    @Override
    public Social createSocial(String email, SocialCreateRequestDTO socialDTO) {
        //엔티티 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        //TagDTO -> String
        List<String> stringTags = socialDTO.getTags().stream()
                .map(TagDTO::getTag)
                .collect(toList());
        log.info(stringTags.toString());
        List<Tag> tags = tagRePository.findAllByNames(stringTags);

        if (tags == null) throw new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_TAG);
        Category category = tags.get(0).getCategory(); //LAZY 로딩

        //이미지 경로 String 1~3개 -> PostImage 생성 및 세팅
        List<PostImage> postImages = socialDTO.getImages().stream()
                .map(dto -> new PostImage(dto.getImagePath()))
                .collect(toList());

        //생성과 동시에 참여
        Socialing socialing = Socialing.createSocialing(user);

        //소셜 생성과 동시에 연관관계 설정
        Social social = Social.createSocial(
                user,
                socialDTO.getTitle(),
                socialDTO.getContents(),
                socialDTO.getContact(),
                socialDTO.getStartDate(),
                socialDTO.getEndDate(),
                socialDTO.getLimitedNums(),
                postImages,
                category,
                tags,
                socialing
        );

        return socialRepository.save(social);
    }

    //모임 게시글 삭제
    @Transactional
    @Override
    public void deleteSocial(Long postId) {
        socialRepository.deleteById(postId);
    }

    //모임 게시글 수정
    @Transactional
    @Override
    public Social modifySocial(String email, Long socialId, SocialModifyRequestDTO socialDTO) {
        //엔티티 조회
        Social social = socialRepository.findById(socialId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));

        // null일 경우에는 수정X
        if (socialDTO.getTitle() != null) social.updateTitle(socialDTO.getTitle());
        if (socialDTO.getContents() != null) social.updateContents(socialDTO.getContents());
        if (socialDTO.getStartDate() != null) social.updateStartDate(socialDTO.getStartDate());
        if (socialDTO.getEndDate() != null) social.updateEndDate(socialDTO.getEndDate());
        if (socialDTO.getLimitedNums() != null) social.updateLimitedNums(socialDTO.getLimitedNums());
        if (socialDTO.getContact() != null) social.updateContact(socialDTO.getContact());

        //이미지
        if (!CollectionUtils.isEmpty(socialDTO.getImages())) {
            //DB에서 PostImage 삭제
            postImageRepository.deleteAllByPostId(social.getId());
            //갈아끼움
            List<PostImage> postImages = socialDTO.getImages().stream()
                    .map(dto -> new PostImage(dto.getImagePath()))
                    .collect(toList());
            social.setImages(postImages);
        }
        return social;
    }

    /**
     * 모임 게시글 단건 조회 (상세 보기)
     *
     * @param postId 게시글 아이디
     * @return SocialLongDTO : 모든 정보가 다 있는 모임 DTO
     */
    @Override
    public SocialLongDTO getSocialDetail(Long userId, Long postId) {
        Social social = socialRepository.findById(postId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));
        SocialLongDTO socialLongDTO = new SocialLongDTO(social);

        socialLongDTO.updateLikeOrElse(likesRepository.existsByUserIdAndPostId(userId, postId));

        return new SocialLongDTO(social);
    }

    /**
     * 모임 게시글 전체 조회 (미리 보기)
     *
     * @return List<SocialShortDTO> : 미리 보기 정보만 있는 모임 리스트
     */
    @Override
    public List<SocialShortDTO> getSocialList(Long userId) {
        //사용자 id 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        List<Social> socialList = socialRepository.findAll(); //social list 가져오기
        List<Like> likes = likesRepository.findByUserId(userId); //like list 가져오기

        System.out.println(user.getRegionCodeFromDongCode());

        //social 리스트 dto 만들기
        List<SocialShortDTO> socialShortDTOS = socialList.stream()
                .filter(social -> social.getRegionCode().equals(user.getRegionCodeFromDongCode()))
                .map(SocialShortDTO::new)
                .collect(toList());

        //만들어진 dto에 좋아요 정보 변경하기
        socialShortDTOS.forEach(socialShortDTO -> checkedLike(socialShortDTO, likes));

        return socialShortDTOS;
    }

    public void checkedLike(SocialShortDTO social, List<Like> likes) {
        for (Like like : likes) {
            if (Objects.equals(like.getPost().getId(), social.getId())) {
                social.updateLikeOrElse(true);
                return;
            }
        }
    }

    /**
     * 내가 작성한 게시글 만 보기
     *
     * @param userId 현재 로그인한 사용자 아이디
     * @return List<SocialShortDTO> : 미리 보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> getMySocialList(Long userId) {
        List<SocialShortDTO> socialShortDTOList = getSocialList(userId);
        return socialShortDTOList.stream().filter(socialShortDTO -> socialShortDTO.getUserId().equals(userId)).collect(toList());

    }

    /**
     * 내가 참여한 모임 게시글 만 보기
     *
     * @param userId 현재 로그인한 사용자 아이디
     * @return List<SocialShortDTO> : 미리 보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> getJoinSocialList(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(()-> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));

        List<SocialShortDTO> socialShortDTOList = getSocialList(userId);
        List<SocialShortDTO> resultDTO = new LinkedList<>();
        for (SocialShortDTO shortDTO : socialShortDTOList) {
            Optional<SocialingDTO> socialing = shortDTO.getSocialings().stream().filter(s -> s.getUserId().equals(userId)).findAny();
            if (!socialing.isEmpty()) {
                resultDTO.add(shortDTO);
            }
        }

        return resultDTO;

//        return socialRepository.findAll().stream()
//                .filter(social -> social.getSocialings().contains(user.getId()))
//                .map(SocialShortDTO::new)
//                .collect(toList());
    }

    /**
     * 모임 게시글 카테고리 필터링
     * @return List<SocialShortDTO> : 카테고리로 필터링된 미리보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> filteringByCategory(Long userId, Long categoryId) {
        List<SocialShortDTO> socialShortDTOList = getSocialList(userId); //내 동에만 있는 리스트 추출

        return socialShortDTOList.stream()
                .filter(socialShortDTO -> socialShortDTO.getCategory().getId().equals(categoryId))
                .collect(toList());
    }

    /**
     * 모임 게시글 태그 필터링 (태그 검색)
     * @param tagId 사용자가 선택한 태그 아이디
     * @return List<SocialShortDTO> : 태그로 필터링된 리스트
     */
    @Override
    public List<SocialShortDTO> filteringByTag(Long tagId) {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();

        for(Social social : socialRepository.findAll()){
            Optional<SocialTag> socialTag = social.getSocialTags().stream()
                    .filter(st -> st.getTag().getId().equals(tagId))
                    .findAny();

            if(!socialTag.isEmpty()){
                socialTag.stream().forEach(tag -> System.out.println(tag.getTag().getId()));
                socialShortDTOList.add(new SocialShortDTO(social));
            }
        }

        return socialShortDTOList;
    }

    /**
     * 리스트 정렬하기
     * @param list 정렬할 리스트
     * @param sortType 1:최신순, 2:마감 임박순, 3:인기순
     * @return
     */
    @Override
    public List<SocialShortDTO> sortByList(List<SocialShortDTO> list, int sortType){
        switch (sortType){
            case 1: //최신순
                return list.stream()
                        .sorted(Comparator.comparing(SocialShortDTO::getCreateDate).reversed())
                        .collect(toList());
            case 2: //마감 임박순
                return list.stream()
                        .sorted(Comparator.comparing(SocialShortDTO::getEndDate))
                        .collect(toList());
            case 3: //인기순
                return list.stream()
                        .sorted(Comparator.comparing(SocialShortDTO::getLikeCnt).reversed())
                        .collect(toList());
            default:
                return list;
        }

    }

    /**
     * 모임 status 변경
     * @param socials 변경할 social list
     */
    public void changeStatus(List<Social> socials){
        LocalDateTime now = LocalDateTime.now();
        for(Social social : socials){
            //available 인 social 중에서 endDate가 현재 시간 보다 이전 시간이면 변경
            if(social.getStatus().equals(SocialStatus.AVAILABLE) && now.isAfter(social.getEndDate())){
                social.changeStatusExpiration();
            }
        }

    }


}
