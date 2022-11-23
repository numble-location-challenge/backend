package com.example.backend.service;

import com.example.backend.dto.LikesDTO;

import java.util.List;

public interface LikeService {

    //사용자의 좋아요 누른 리스트
    List<LikesDTO> getLikes(Long userId);
    //좋아요 선택
    LikesDTO onLike(Long userId, Long postId);
    //좋아요 취소
    void offLike(Long userId, Long postId);
}
