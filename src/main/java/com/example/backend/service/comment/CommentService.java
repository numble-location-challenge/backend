package com.example.backend.service.comment;

import java.util.List;

import com.example.backend.domain.Comment;
import com.example.backend.dto.comment.CommentRequestDTO;
import com.example.backend.dto.comment.CommentResponseDTO;

public interface CommentService {

    List<Comment> getComments(Long postId); // 댓글 리스트 출력

    void createComment(Long postId, String userEmail, CommentRequestDTO commentRequestDTO); // 댓글 생성

    void createReply(Long postId, String userEmail,Long commentId, CommentRequestDTO commentRequestDTO); // 대댓글 생성

    void deleteComment(Long commentId, String userEmail); // 댓글 및 대댓글 삭제

    CommentResponseDTO updateComment(Long commentId, String userEmail, CommentRequestDTO commentRequestDTO); // 댓글 및 대댓글 수정

}
