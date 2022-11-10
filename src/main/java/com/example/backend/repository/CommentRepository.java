package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByPost_Id(Long postId);
}
