package com.example.backend.service.social;

import com.example.backend.dto.social.SocialCreateRequestDTO;
import com.example.backend.dto.social.SocialLongDTO;
import com.example.backend.dto.social.SocialModifyRequestDTO;
import com.example.backend.dto.social.SocialShortDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SocialService {
    //모임 게시글 생성
    void createSocial(String email, SocialCreateRequestDTO socialDTO);

    //모임 게시글 삭제
    void deleteSocial(Long postId);

    //모임 게시글 수정
    void updateSocial(String email, Long socialId, SocialModifyRequestDTO socialDTO);

    //모임 게시글 1개 출력 (상세보기)
    SocialLongDTO getSocialDetail(Long socialId);

    //모임 게시글 리스트 출력 (미리보기)
    List<SocialShortDTO> getSocialList();

    //본인이 작성한 모임 게시글만 출력
    List<SocialShortDTO> getMySocialList(Long userId);

    //본인이 참여한 모임 게시글만 출력
    List<SocialShortDTO> getJoinSocialList(Long userId);

    //카테고리 필터링
    List<SocialShortDTO> filteringByCategory(Long CategoryId);

    //태그 필터링
    List<SocialShortDTO> filteringByTag(List<Long> TagId);

    //최신순 정렬
    List<SocialShortDTO> sortByLatest();

    //마감순 정렬
    List<SocialShortDTO> sortByClosing();

    //인기순 정렬
    List<SocialShortDTO> SortByPopularity();
}
