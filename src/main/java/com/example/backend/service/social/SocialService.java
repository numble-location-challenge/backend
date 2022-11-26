package com.example.backend.service.social;

import com.example.backend.domain.post.Social;
import com.example.backend.dto.social.SocialCreateRequestDTO;
import com.example.backend.dto.social.SocialLongDTO;
import com.example.backend.dto.social.SocialModifyRequestDTO;
import com.example.backend.dto.social.SocialShortDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SocialService {
    //모임 게시글 생성
    Social createSocial(String email, SocialCreateRequestDTO socialDTO);

    //모임 게시글 삭제
    void deleteSocial(Long postId);

    //모임 게시글 수정
    Social modifySocial(String email, Long socialId, SocialModifyRequestDTO socialDTO);

    //모임 게시글 1개 출력 (상세보기)
    SocialLongDTO getSocialDetail(Long socialId);

    //모임 게시글 리스트 출력 (미리보기)
    List<SocialShortDTO> getSocialList(String email);

    //본인이 작성한 모임 게시글만 출력
    List<SocialShortDTO> getMySocialList(String email);

    //본인이 참여한 모임 게시글만 출력
    List<SocialShortDTO> getJoinSocialList(String email);

    //카테고리 필터링
    List<SocialShortDTO> filteringByCategory(String email, Long CategoryId);

    //태그 필터링
    List<SocialShortDTO> filteringByTag(Long TagId);

    //리스트 정렬
    List<SocialShortDTO> sortByList(List<SocialShortDTO> list, int sortType);
}
