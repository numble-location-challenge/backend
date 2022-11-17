package com.example.backend.service.social;

import com.example.backend.domain.Socialing;
import com.example.backend.domain.post.Social;
import com.example.backend.dto.social.SocialLongDTO;
import com.example.backend.dto.social.SocialShortDTO;
import com.example.backend.repository.SocialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialServiceImpl implements SocialService {

    private final SocialRepository socialRepository;

    //모임 게시글 생성
    @Override
    public void createSocial(SocialLongDTO socialLongDTO) {

    }

    //모임 게시글 삭제
    @Override
    public void deleteSocial(Long postId) {
        socialRepository.deleteById(postId);
    }


    //모임 게시글 수정
    @Override
    public void updateSocial(SocialLongDTO socialLongDTO) {

    }

    /**
     * 모임 게시글 단건 조회 (상세 보기)
     * @param postId 게시글 아이디
     * @return SocialLongDTO : 모든 정보가 다 있는 모임 DTO
     */
    @Override
    public SocialLongDTO getSocialDetail(Long postId) {
        Social social = socialRepository.findByPostId(postId).orElseThrow();
        SocialLongDTO socialLongDTO = new SocialLongDTO(social);
        return socialLongDTO;
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
            if(socialing != null){
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
        while(socialRepository.findByTagId(tagId).stream().iterator().hasNext()){
            Social social = socialRepository.findByTagId(tagId).stream().iterator().next();
            socialShortDTOList.add(new SocialShortDTO(social));
        }
        return socialShortDTOList;
    }

    /**
     * 최신순 정렬
     * @return List<SocialShortDTO> : 최신순으로 정렬된 미리보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> sortByLatest() {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        while(socialRepository.findAllOrderByCreateDate().iterator().hasNext()){
            Social social = socialRepository.findAllOrderByCreateDate().iterator().next();
            socialShortDTOList.add(new SocialShortDTO(social));
        }
        return socialShortDTOList;
    }

    /**
     * 마감 임박 순 정렬
     * @return List<SocialShortDTO> : 마감임박순으로 정렬된 미리보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> sortByClosing() {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        while(socialRepository.findAllOrderByEndDate().iterator().hasNext()){
            Social social = socialRepository.findAllOrderByEndDate().iterator().next();
            socialShortDTOList.add(new SocialShortDTO(social));
        }
        return socialShortDTOList;
    }

    /**
     * 인기순 정렬
     * @return List<SocialShortDTO> : 인기순으로 정렬된 미리보기 형식의 리스트
     */
    @Override
    public List<SocialShortDTO> SortByPopularity() {
        List<SocialShortDTO> socialShortDTOList = new LinkedList<>();
        while(socialRepository.findAllOrderByLikes().iterator().hasNext()){
            Social social = socialRepository.findAllOrderByLikes().iterator().next();
            socialShortDTOList.add(new SocialShortDTO(social));
        }
        return socialShortDTOList;
    }
}
