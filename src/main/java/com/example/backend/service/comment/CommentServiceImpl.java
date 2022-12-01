package com.example.backend.service.comment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.Comment;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Post;
import com.example.backend.dto.comment.CommentRequestDTO;
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


    @Override
    @Transactional(readOnly = true)
    public List<Comment> getComments(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCGroupDescRefOrderAsc(postId);
        return comments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getMyComments(Long userId) {
        User user = userRepository.findReadOnlyById(userId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        List<Comment> comments = commentRepository.findAllByUserIdAndDeletedIsFalse(user.getId());
        return comments;
    }

    @Transactional
    @Override
    public void createComment(Long postId, Long userId, CommentRequestDTO commentRequestDTO) {
        User user = userRepository.findReadOnlyById(userId)
            .orElseThrow(()-> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        Post post = postRepository.findById(postId)
            .orElseThrow(()-> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_POST));
        Integer newCommentGroup = commentRepository.findByMaxCommentGroup() + 1;
        Comment comment = Comment.createComment(commentRequestDTO.getContents(),
            newCommentGroup, 0, 0, 0L, post, user);
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void createReply(Long postId, Long userId, Long commentId, CommentRequestDTO commentRequestDTO) {
        User user = userRepository.findReadOnlyById(userId)
            .orElseThrow(()-> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        Post post = postRepository.findById(postId)
            .orElseThrow(()-> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_POST));
        Comment parent_comment = commentRepository.findByIdAndPostIdAndDeletedIsFalse(commentId, postId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_COMMENT));

        if (isComment(parent_comment)) {
            int parentLevel = parent_comment.getLevel();
            int parentCGroup = parent_comment.getCGroup();
            int lastRefOrder = commentRepository.findLastRefOrderInGroup(parentCGroup, postId);
            Comment comment = Comment.createComment(commentRequestDTO.getContents(), parentCGroup,
                parentLevel + 1, lastRefOrder + 1, parent_comment.getId(), post, user);
            commentRepository.save(comment);
        } else{
            throw new CommentInvalidInputException(CommentInvalidInputExceptionType.INVALID_INPUT_COMMENT_ID);
        }
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findByIdAndDeletedIsFalse(commentId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_COMMENT));
        Long countCommentByCGroup = commentRepository.countBycGroup(comment.getCGroup());
        if (hasPermission(comment, userId)) {
            if (isComment(comment)) {
                if (countCommentByCGroup > 1){
                    comment.setDeleted();
                } else{
                    commentRepository.delete(comment);
                }
            } else {
                commentRepository.delete(comment);
            }
        } else {
            throw new ForbiddenException(ForbiddenExceptionType.NOT_AUTHORITY_DELETE_COMMENT);
        }
    }

    @Transactional
    public Comment updateComment(Long commentId, Long userId, CommentRequestDTO commentRequestDTO) {
        Comment comment = commentRepository.findByIdAndDeletedIsFalse(commentId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_COMMENT));
        if (hasPermission(comment, userId)) {
            comment.updateComment(commentRequestDTO.getContents());
        } else {
            throw new ForbiddenException(ForbiddenExceptionType.NOT_AUTHORITY_UPDATE_COMMENT);
        }
        return comment;
    }

    private boolean hasPermission(Comment comment, Long userId) {
        if (comment.getUser().getId().equals(userId)) {
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
