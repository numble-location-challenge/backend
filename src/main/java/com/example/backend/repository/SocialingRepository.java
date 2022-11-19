package com.example.backend.repository;

import com.example.backend.domain.Socialing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SocialingRepository extends JpaRepository<Socialing, Long> {

    void deleteByUserIdAndSocialId(Long userId, Long socialId);

}
