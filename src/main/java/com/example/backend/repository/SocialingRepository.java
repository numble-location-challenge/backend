package com.example.backend.repository;

import com.example.backend.domain.Socialing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SocialingRepository extends JpaRepository<Socialing, Long> {

    @Query("delete from Socialing s where s.user=:userId and s.social=:socialId")
    Socialing deleteByUserIdAndSocialId(@Param("userId") Long userId, @Param("socialId") Long socialId);

}
