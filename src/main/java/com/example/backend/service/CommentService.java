package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.domain.Comment;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Feed;
import com.example.backend.dto.CommentRequestDTO;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.FeedRepository;
import com.example.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    public List<Comment> getComments(Long postId) {
        List<Comment> comments = commentRepository.findAllByPost_Id(postId);
        return comments;

    }

    public void createComment(Long postId, Long userId, CommentRequestDTO commentRequestDTO) {
        User user = userRepository.findById(userId).orElseThrow();
        Feed feed = feedRepository.findById(postId).orElseThrow();


    }
}
