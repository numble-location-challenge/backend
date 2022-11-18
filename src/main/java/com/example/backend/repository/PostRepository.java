package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.domain.post.Feed;

public interface PostRepository extends JpaRepository<Feed, Long> {
}
