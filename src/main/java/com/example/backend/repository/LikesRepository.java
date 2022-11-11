package com.example.backend.repository;

import com.example.backend.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Like,Long> {

}
