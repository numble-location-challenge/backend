package com.example.backend.service.social;

import com.example.backend.domain.PostImage;
import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;

import com.example.backend.domain.post.Social;
import com.example.backend.domain.tag.Category;
import com.example.backend.domain.tag.SocialTag;
import com.example.backend.domain.tag.Tag;
import com.example.backend.dto.PostImageDTO;
import com.example.backend.dto.TagDTO;
import com.example.backend.dto.social.SocialCreateRequestDTO;
import com.example.backend.domain.tag.SocialTag;
import com.example.backend.dto.social.SocialLongDTO;
import com.example.backend.dto.social.SocialModifyRequestDTO;
import com.example.backend.dto.social.SocialShortDTO;
import com.example.backend.global.exception.EntityNotExistsException;
import com.example.backend.global.exception.EntityNotExistsExceptionType;
import com.example.backend.repository.PostImageRepository;
import com.example.backend.repository.SocialRepository;
import com.example.backend.repository.TagRepository;
import com.example.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SocialServiceImpl implements SocialService {

    private final UserRepository userRepository;
    private final SocialRepository socialRepository;
    private final TagRepository tagRePository;
    private final PostImageRepository postImageRepository;

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

        if(tags == null) throw new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_TAG);
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
    public void modifySocial(String email, Long socialId, SocialModifyRequestDTO socialDTO) {
        //엔티티 조회
        Social social = socialRepository.findById(socialId)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));

        // null일 경우에는 수정X
        if(socialDTO.getTitle() != null) social.updateTitle(socialDTO.getTitle());
        if(socialDTO.getContents() != null) social.updateContents(socialDTO.getContents());
        if(socialDTO.getStartDate() != null) social.updateStartDate(socialDTO.getStartDate());
        if(socialDTO.getEndDate() != null) social.updateEndDate(socialDTO.getEndDate());
        if(socialDTO.getLimitedNums() != null) social.updateLimitedNums(socialDTO.getLimitedNums());
        if(socialDTO.getContact() != null) social.updateContact(socialDTO.getContact());

        //이미지
        if(!CollectionUtils.isEmpty(socialDTO.getImages())){
            //DB에서 PostImage 삭제
            postImageRepository.deleteAllByPostId(social.getId());
            //갈아끼움
            List<PostImage> postImages = socialDTO.getImages().stream()
                    .map(dto -> new PostImage(dto.getImagePath()))
                    .collect(toList());
            social.setImages(postImages);
        }

    }

    /**
     * 모임 게시글 단건 조회 (상세 보기)
     * @param postId 게시글 아이디
     * @return SocialLongDTO : 모든 정보가 다 있는 모임 DTO
     */
    @Override
    public SocialLongDTO getSocialDetail(Long postId) {
        Social social = socialRepository.findById(postId).orElseThrow(()->new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));
        return new SocialLongDTO(social);
    }

    /**
     * 모임 게시글 전체 조회 (미리 보기)
     * @return List<SocialShortDTO> : 미리 보기 정보만 있는 모임 리스트
     */
    @Override
    public List<SocialShortDTO> getSocialList() {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        for(Social social : socialRepository.findAll()){
            socialShortDTOList.add(new SocialShortDTO(social));
        }
        return socialShortDTOList;
    }

    /**
     * 내가 작성한 게시글 만 보기
     * @param userId 사용자 아이디
     * @return List<SocialShortDTO> : 미리 보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> getMySocialList(Long userId) {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        for(Social social : socialRepository.findAll()){
            if(Objects.equals(social.getUser().getId(), userId))
                socialShortDTOList.add(new SocialShortDTO(social));
        }
        return socialShortDTOList;
    }

    /**
     * 내가 참여한 모임 게시글 만 보기
     * @param userId 사용자 아이디
     * @return List<SocialShortDTO> : 미리 보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> getJoinSocialList(Long userId) {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        for(Social social : socialRepository.findAll()){
            Optional<Socialing> socialing = social.getSocialings().stream().filter(s -> Objects.equals(s.getUser().getId(), userId)).findFirst();
            if(socialing.isPresent()){
                socialShortDTOList.add(new SocialShortDTO(social));
            }
        }
        return socialShortDTOList;
    }

    /**
     * 모임 게시글 카테고리 필터링 (안쓰는거 같아서 수정안함)
     * @return List<SocialShortDTO> : 카테고리로 필터링된 미리보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> filteringByCategory(Long categoryId) {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        for(Social social : socialRepository.findAll()){
            if(Objects.equals(social.getCategory().getId(),categoryId))
                socialShortDTOList.add(new SocialShortDTO(social));
        }
        return socialShortDTOList;
    }

    /**
     * 모임 게시글 태그 필터링
     * @param tagId 사용자가 선택한 태그 아이디
     * @return List<SocialShortDTO> : 태그로 필터링된 리스트
     */
    @Override
    public List<SocialShortDTO> filteringByTag(Long tagId) {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        for(Social social : socialRepository.findAll()){
            Optional<SocialTag> socialTag = social.getSocialTags().stream().filter(st -> st.getTag().getId().equals(tagId)).findFirst();
            if(socialTag.isPresent()){
                socialShortDTOList.add(new SocialShortDTO(social));
            }
        }
        return socialShortDTOList;
    }

    /**
     * 리스트 정렬
     * @param sortDirection 오름차순, 내림차순 기준 (true ? DESC : ASC)
     * @param properties 정렬할 기준
     * @return List<SocialShortDTO> : 정렬된 미리보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> sortByList(Boolean sortDirection, String properties){
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        if(properties.equals("")){
            return getSocialList();
        }else{
            List<Social> socialList = socialRepository.findAll(Sort.by(sortDirection ? Sort.Direction.DESC : Sort.Direction.ASC, properties));
            for(Social social : socialList){
                socialShortDTOList.add(new SocialShortDTO(social));
            }
            return socialShortDTOList;
        }

    }

}
