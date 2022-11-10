package com.example.backend.repository;

import com.example.backend.dto.SocialDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SocialRepository extends CrudRepository<SocialDTO, Long>, JpaRepository<SocialDTO, Long> {

}
