package com.example.backend.service;

import com.example.backend.dto.LikesDTO;

public interface LikeService {

    LikesDTO onLike(LikesDTO likesDTO);

    LikesDTO offLike(LikesDTO likesDTO);
}
