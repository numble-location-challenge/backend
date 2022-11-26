package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("SELECT c FROM Comment c join fetch c.user where c.post.id =:postId order by c.cGroup desc, c.refOrder asc")
    List<Comment> findAllByPostIdOrderByCGroupDescRefOrderAsc(@Param("postId") Long postId);

    @Query("SELECT coalesce(Max(c.cGroup),0) FROM Comment c")
    Integer findByMaxCommentGroup();

    @Query("SELECT max(c.refOrder) FROM Comment c where c.cGroup =:cGroup and c.post.id =:post_id")
    Integer findLastRefOrderInGroup(@Param("cGroup") int cGroup, @Param("post_id") Long postId);

    Optional<Comment> findByIdAndDeletedIsFalse(Long id);

    Long countBycGroup(int cGroup);

    Optional<Comment> findByIdAndPostIdAndDeletedIsFalse(Long id, Long postId);

    List<Comment> findAllByUserId(Long userId);
}
