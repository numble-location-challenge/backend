package com.example.backend.repository;

import com.example.backend.domain.post.Social;
import org.springframework.data.jpa.repository.*;

import javax.persistence.QueryHint;
import java.util.Optional;

public interface SocialRepository extends JpaRepository<Social,Long> {

    //게시글 아이디로 찾기 (단건 조회)
    Optional<Social> findById(Long postId);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Optional<Social> findReadOnlyById(Long socialId);

    //자식타입 조회시 부모와 조인해서 조회됨
    void deleteAllByUserId(Long userId);
}
