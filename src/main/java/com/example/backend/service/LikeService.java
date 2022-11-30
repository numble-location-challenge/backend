package com.example.backend.service;

import com.example.backend.dto.LikesDTO;

import java.util.List;
import java.util.Optional;

public interface LikeService {

    //좋아요 선택
    LikesDTO likeOnAndOff(Long userId, Long postId);
}
