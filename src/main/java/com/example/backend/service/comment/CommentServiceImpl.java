package com.example.backend.service.comment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.Comment;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Post;
import com.example.backend.dto.comment.CommentRequestDTO;
import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.dto.comment.CommentUpdateDTO;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //TODO 최근에 달린 댓글이 더 상단에 표시되도록 쿼리 작성
    @Override
    public List<Comment> getComments(Long postId) {
        List<Comment> comments = commentRepository.findAllByPost_Id(postId);
        return comments;
    }

    @Transactional
    @Override
    public void createComment(Long postId, String userEmail, CommentRequestDTO commentRequestDTO) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        Integer newCommentGroup = commentRepository.findByMaxCommentGroup() + 1;
        //댓글 그룹 번호 NULL이면 0, 아니면 최댓값
        // commentId가 null 이면 댓글, 아니면 대댓글 저장

        Comment comment = Comment.createComment(commentRequestDTO.getContents(), newCommentGroup, 0,
            0, 0L, post, user);
        commentRepository.save(comment);

    }

    //TODO 댓글이 아니면 대댓글을 달수 없도록 로직 작성
    @Transactional
    @Override
    public void createReply(Long postId, String userEmail, CommentRequestDTO commentRequestDTO) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        Comment parent_comment = commentRepository.findByIdAndPostId(commentRequestDTO.getCommentId(), postId); // 부모 댓글의 정보
        if (isComment(parent_comment)) {
            int parentLevel = parent_comment.getLevel(); // 부모 댓글의 레벨
            int parentCGroup = parent_comment.getCGroup(); // 부모 댓글의 그룹
            int lastReforder = commentRepository.findLastRefOrderInGroup(parentCGroup, postId);

            Comment comment = Comment.createComment(commentRequestDTO.getContents(), parentCGroup, parentLevel + 1,
                lastReforder + 1, parent_comment.getId(), post, user);
            commentRepository.save(comment);
        }
    }

    @Override
    public void deleteComment(Long commentId, String userEmail) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (hasPermission(comment, userEmail)) {
            if (isComment(comment)) {
                commentRepository.deleteAllByCgroup(comment.getCGroup());
            } else {
                commentRepository.delete(comment);
            }
        }
    }

    @Transactional
    public CommentResponseDTO updateComment(Long commentId, String userEmail, CommentUpdateDTO commentUpdateDTO) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (hasPermission(comment, userEmail)) {
            comment.updateComment(commentUpdateDTO.getContents());
            CommentResponseDTO responseDTO = new CommentResponseDTO(comment);
            return responseDTO;
        } else {
            throw new RuntimeException("권한이 없습니다.");
        }
    }

    private boolean hasPermission(Comment comment, String userEmail) {
        if (comment.getUser().getEmail().equals(userEmail)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isComment(Comment comment){
        if (comment.getLevel() == 0){
            return true;
        } else{
            return false;
        }
    }
}
