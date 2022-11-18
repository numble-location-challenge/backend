package com.example.backend.service.social;

import com.example.backend.domain.PostImage;
import com.example.backend.domain.Socialing;
import com.example.backend.domain.User;

import com.example.backend.domain.post.Social;
import com.example.backend.domain.tag.Category;
import com.example.backend.domain.tag.SocialTag;
import com.example.backend.domain.tag.Tag;
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
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialServiceImpl implements SocialService {

    private final UserRepository userRepository;
    private final SocialRepository socialRepository;
    private final TagRepository tagRePository;
    private final PostImageRepository postImageRepository;

    //모임 게시글 생성
    @Transactional
    @Override
    public void createSocial(String email, SocialCreateRequestDTO socialDTO) {
        //엔티티 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        List<Tag> tags = tagRePository.findAllByNames(socialDTO.getTags());
        //TODO tags 안의 카테고리 id가 모두 같아야함. 하나라도 다르면 예외처리
        Category category = tags.get(0).getCategory(); //LAZY 로딩

        //이미지 경로 String 1~3개 -> PostImage 생성 및 세팅
        List<PostImage> postImages = socialDTO.getImages().stream()
                .map(dto -> new PostImage(dto.getImagePath()))
                .collect(toList());

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
                tags
        );

        //생성과 동시에 참여
        Socialing.createSocialing(user, social);

        socialRepository.save(social);
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
    public void updateSocial(String email, Long socialId, SocialModifyRequestDTO socialDTO) {
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
                    .map(PostImage::new)
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
        Social social = socialRepository.findById(postId).orElseThrow();
        return new SocialLongDTO(social);
    }

    /**
     * 모임 게시글 리스트 조회 (미리 보기)
     * @return List<SocialShortDTO> : 미리 보기 정보만 있는 모임 리스트
     */
    @Override
    public List<SocialShortDTO> getSocialList() {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        while(socialRepository.findAll().iterator().hasNext()){
            Social social = socialRepository.findAll().iterator().next();
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
        while(socialRepository.findByUserId(userId).stream().iterator().hasNext()){
            Social social = socialRepository.findByUserId(userId).stream().iterator().next();
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
        while(socialRepository.findByUserId(userId).stream().iterator().hasNext()){
            Social social = socialRepository.findByUserId(userId).stream().iterator().next();
            Optional<Socialing> socialing = social.getSocialings().stream().filter(s -> Objects.equals(s.getUser().getId(), userId)).findFirst();
            if(socialing.isPresent()){
                socialShortDTOList.add(new SocialShortDTO(social));
            }
        }
        return socialShortDTOList;
    }

    /**
     * 모임 게시글 카테고리 필터링
     * @return List<SocialShortDTO> : 카테고리로 필터링된 미리보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> filteringByCategory(Long categoryId) {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        while(socialRepository.findByCategoryId(categoryId).stream().iterator().hasNext()){
            Social social = socialRepository.findByCategoryId(categoryId).stream().iterator().next();
            socialShortDTOList.add(new SocialShortDTO(social));
        }
        return socialShortDTOList;
    }

    /**
     * 모임 게시글 태그 필터링
     * @param tagId 사용자가
     * @return List<SocialShortDTO> : 태그로 필터링된 미리보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> filteringByTag(Long tagId) {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        while(socialRepository.findAll().iterator().hasNext()){
            Social social = socialRepository.findAll().iterator().next();
            Optional<SocialTag> socialTag = social.getSocialTags().stream().filter(tag -> Objects.equals(tag.getId(),tagId)).findFirst();
            if(socialTag.isPresent()){
                socialShortDTOList.add(new SocialShortDTO(social));
            }
        }
        return socialShortDTOList;
    }

    /**
     * 리스트 정렬
     * @param properties 정렬할 기준
     * @return List<SocialShortDTO> : 정렬된 미리보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> sortByList(String properties){
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        if(properties.equals("")){
            while(socialRepository.findAll().iterator().hasNext()){
                Social social = socialRepository.findAll().iterator().next();
                socialShortDTOList.add(new SocialShortDTO(social));
            }
        }else{
            List<Social> socialList = socialRepository.findAll(Sort.by(Sort.Direction.DESC,properties));
            for(Social social : socialList){
                socialShortDTOList.add(new SocialShortDTO(social));
            }
        }

        return socialShortDTOList;
    }

}
