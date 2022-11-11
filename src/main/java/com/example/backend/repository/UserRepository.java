package com.example.backend.repository;

import com.example.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);//이메일로 회원 조회

    boolean existsByEmailAndNickname(String email, String nickName);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickName);
}
