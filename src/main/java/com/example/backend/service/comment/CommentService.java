package com.example.backend.service.comment;

import java.util.List;

import com.example.backend.domain.Comment;
import com.example.backend.dto.comment.CommentRequestDTO;

public interface CommentService {

    List<Comment> getComments(Long postId); // 댓글 리스트 출력

    void createComment(Long postId, Long userId, CommentRequestDTO commentRequestDTO); // 댓글 생성

    void createReply(Long postId, Long userId, Long commentId, CommentRequestDTO commentRequestDTO); // 대댓글 생성

    void deleteComment(Long commentId, Long userId); // 댓글 및 대댓글 삭제

    Comment updateComment(Long commentId, Long userId, CommentRequestDTO commentRequestDTO); // 댓글 및 대댓글 수정

    List<Comment> getMyComments(Long userId);
}
