package com.example.backend.repository;

import com.example.backend.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Like,Long> {

    //user id로 찾기
    @Query("SELECT l FROM Like l WHERE l.user.id = :userId")
    List<Like> findByUserId(Long userId);

    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.post.id = :postId")
    Optional<Like> findById(Long userId, Long postId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Like l WHERE l.user.id = :userId and l.post.id = :postId")
    void deleteById(Long userId, Long postId);
}
