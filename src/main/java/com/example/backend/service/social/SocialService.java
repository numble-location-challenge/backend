package com.example.backend.service.social;

import com.example.backend.dto.social.SocialLongDTO;
import com.example.backend.dto.social.SocialShortDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SocialService {
    //모임 게시글 생성
    void createSocial(SocialLongDTO socialLongDTO);

    //모임 게시글 삭제
    void deleteSocial(Long postId);

    //모임 게시글 수정
    void updateSocial(SocialLongDTO socialLongDTO);

    //모임 게시글 1개 출력 (상세보기)
    SocialLongDTO getSocialDetail(Long socialId);

    //모임 게시글 리스트 출력 (미리보기)
    List<SocialShortDTO> getSocialList();

    //본인이 작성한 모임 게시글만 출력
    List<SocialShortDTO> getMySocialList();

    //본인이 참여한 모임 게시글만 출력
    List<SocialShortDTO> getJoinSocialList();


}
