package com.example.backend.service.comment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.Comment;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Post;
import com.example.backend.dto.comment.CommentRequestDTO;
import com.example.backend.dto.comment.CommentResponseDTO;
import com.example.backend.global.exception.EntityNotExistsException;
import com.example.backend.global.exception.EntityNotExistsExceptionType;
import com.example.backend.global.exception.ForbiddenException;
import com.example.backend.global.exception.ForbiddenExceptionType;
import com.example.backend.global.exception.comment.CommentInvalidInputException;
import com.example.backend.global.exception.comment.CommentInvalidInputExceptionType;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getComments(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCGroupDescRefOrderAsc(postId);
        return comments;
    }

    @Transactional
    @Override
    public void createComment(Long postId, String userEmail, CommentRequestDTO commentRequestDTO) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new EntityNotExistsException(
            EntityNotExistsExceptionType.NOT_FOUND_USER));
        Post post = postRepository.findById(postId).orElseThrow(()-> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_POST));
        Integer newCommentGroup = commentRepository.findByMaxCommentGroup() + 1;
        //댓글 그룹 번호 NULL이면 0, 아니면 최댓값
        Comment comment = Comment.createComment(commentRequestDTO.getContents(), newCommentGroup, 0,
            0, 0L, post, user);
        commentRepository.save(comment);

    }

    @Transactional
    @Override
    public void createReply(Long postId, String userEmail, Long commentId, CommentRequestDTO commentRequestDTO) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new EntityNotExistsException(
            EntityNotExistsExceptionType.NOT_FOUND_USER));
        Post post = postRepository.findById(postId).orElseThrow(()-> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_POST));
        Comment parent_comment = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_COMMENT)); // 부모 댓글의 정보
        if (isComment(parent_comment)) {
            int parentLevel = parent_comment.getLevel(); // 부모 댓글의 레벨
            int parentCGroup = parent_comment.getCGroup(); // 부모 댓글의 그룹
            int lastReforder = commentRepository.findLastRefOrderInGroup(parentCGroup, postId);

            Comment comment = Comment.createComment(commentRequestDTO.getContents(), parentCGroup, parentLevel + 1,
                lastReforder + 1, parent_comment.getId(), post, user);
            commentRepository.save(comment);
        } else{
            throw new CommentInvalidInputException(CommentInvalidInputExceptionType.INVALID_INPUT_COMMENT_ID);
        }
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, String userEmail) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_COMMENT));
        if (hasPermission(comment, userEmail)) {
            if (isComment(comment)) {
                commentRepository.deleteAllByCgroup(comment.getCGroup());
            } else {
                commentRepository.delete(comment);
            }
        } else {
            throw new ForbiddenException(ForbiddenExceptionType.NOT_AUTHORITY_DELETE_COMMENT);
        }
    }

    @Transactional
    public CommentResponseDTO updateComment(Long commentId, String userEmail, CommentRequestDTO commentRequestDTO) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_COMMENT));
        if (hasPermission(comment, userEmail)) {
            comment.updateComment(commentRequestDTO.getContents());
            CommentResponseDTO responseDTO = new CommentResponseDTO(comment);
            return responseDTO;
        } else {
            throw new ForbiddenException(ForbiddenExceptionType.NOT_AUTHORITY_UPDATE_COMMENT);
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
