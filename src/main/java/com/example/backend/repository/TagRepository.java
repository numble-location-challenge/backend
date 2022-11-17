package com.example.backend.repository;

import com.example.backend.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    //태그 이름으로 찾기
    @Query("select t from Tag t where t.name in :names")
    List<Tag> findAllByNames(@Param("names") Collection<String> names);
}
