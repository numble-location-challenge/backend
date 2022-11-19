package com.example.backend.repository;

import com.example.backend.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.domain.post.Feed;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findReadOnlyById(Long socialId);
}
