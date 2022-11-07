package com.example.backend.repository;

import com.example.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);//이메일로 회원 조회
    User findByNickname(String email);//닉네임으로 회원 조회
}
