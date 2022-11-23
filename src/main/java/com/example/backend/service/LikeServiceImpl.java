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

    @Override
    public List<LikesDTO> getLikes(Long userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        List<LikesDTO> likes = likesRepository.findByUserId(user.getId())
                .stream()
                .map(LikesDTO::new)
                .collect(Collectors.toList());
        return likes;
    }

    @Override
    public LikesDTO onLike(Long userId, Long postId) {
        //user id, post id 검증
        User user = userRepository.findById(userId).orElseThrow( () -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        Post post = postRepository.findById(postId).orElseThrow( () -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_POST));
        //좋아요가 이미 있는지 확인
        Optional<Like> existentLike = likesRepository.findById(user.getId(),post.getId());
        if(existentLike.isEmpty()){
            //좋아요 저장
            Like like = Like.createLike(user, post);
            likesRepository.save(like);
            //좋아요 개수 카운터
            int likeCnt = post.getLikes();
            post.updateLikeCnt(likeCnt+1);

            LikesDTO likesDTO = new LikesDTO(like);
            return likesDTO;
        }else{
            offLike(userId,postId);
            return null;
        }

    }

    @Override
    public void offLike(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        Post post = postRepository.findById(postId).orElseThrow( () -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_POST));
        //likes에서 삭제
        likesRepository.deleteById(user.getId(),post.getId());
        //post에서 likes-1
        int likeCnt = post.getLikes();
        post.updateLikeCnt(likeCnt-1);

//        return null;
    }
}
