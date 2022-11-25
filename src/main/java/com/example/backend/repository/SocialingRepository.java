package com.example.backend.repository;

import com.example.backend.domain.Socialing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialingRepository extends JpaRepository<Socialing, Long> {

    void deleteByUserIdAndSocialId(Long userId, Long socialId);

    void deleteAllByUserId(Long userId);
}
