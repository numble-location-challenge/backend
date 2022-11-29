package com.example.backend.repository;

import com.example.backend.domain.User;
import com.example.backend.domain.enumType.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);//이메일로 회원 조회
    Optional<User> findBySnsIdAndUserType(Long id, UserType userType);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Optional<User> findReadOnlyById(Long userId); //더티체킹 필요없이 조회만 필요할 때 사용

    boolean existsByEmailAndNickname(String email, String nickName);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickName);

}
