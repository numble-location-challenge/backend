package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByPost_Id(Long postId);

    @Query("SELECT coalesce(Max(c.cGroup),0) FROM Comment c")
    Integer findByMaxCommentGroup();

    @Query("SELECT max(c.refOrder) FROM Comment c where c.cGroup =:cGroup and c.post.id =:post_id")
    Integer findLastRefOrderInGroup(@Param("cGroup") int cGroup, @Param("post_id") Long postId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Comment c where c.cGroup =:cGroup")
    void deleteAllByCgroup(@Param("cGroup")int cGroup);

    Comment findByIdAndPostId(Long id, Long postId);
}
