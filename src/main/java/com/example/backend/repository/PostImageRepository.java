package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.domain.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage,Long> {
}
