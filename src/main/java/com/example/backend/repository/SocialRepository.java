package com.example.backend.repository;

import com.example.backend.domain.post.Social;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SocialRepository extends JpaRepository<Social,Long> {

    //게시글 아이디로 찾기
    Optional<Social> findById(Long postId);

    //사용자 아이디로 찾기
    Optional<Social> findByUserId(Long userId);

    //카테고리 아이디로 찾기
    Optional<Social> findByCategoryId(Long categoryId);
}
