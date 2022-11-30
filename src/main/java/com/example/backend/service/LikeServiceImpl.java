package com.example.backend.service;

import com.example.backend.domain.Like;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Post;
import com.example.backend.domain.post.Social;
import com.example.backend.dto.LikesDTO;
import com.example.backend.global.exception.EntityNotExistsException;
import com.example.backend.global.exception.EntityNotExistsExceptionType;
import com.example.backend.repository.LikesRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.SocialRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikesDTO likeOnAndOff(Long userId, Long postId){
        //user id, post id 검증
        User user = userRepository.findById(userId).orElseThrow( () -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        Post post = postRepository.findById(postId).orElseThrow( () -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_POST));
        int likeCnt = post.getLikes(); //좋아요 개수

        //좋아요가 이미 있는지 확인
        Optional<LikesDTO> existentLike = likesRepository.findById(user.getId(),post.getId()).map(LikesDTO::new);
        LikesDTO likesDTO = null;

        if(existentLike.isEmpty()){//좋아요가 없다면 좋아요 등록
            Like like = Like.createLike(user,post);
            likesRepository.save(like);
            likesDTO = new LikesDTO(like);
            likeCnt++;
            likesDTO.updateOrElse(true);
        }else{//좋아요가 있다면 좋아요 해제
            likesDTO = existentLike.get();
            likesRepository.deleteById(user.getId(),post.getId());
            likeCnt--;
            likesDTO.updateOrElse(false);
        }

        post.updateLikes(likeCnt);
        postRepository.save(post);

        return likesDTO;

    }

}
