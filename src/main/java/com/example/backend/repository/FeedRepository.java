package com.example.backend.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.domain.post.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @Query("select f from Feed f join fetch f.user where f.regionCode =:regionCode order by f.createDate desc")
    Slice<Feed> findAllFeedsLATEST(@Param("regionCode") int reginCode, Pageable pageable);

    @Query("select f from Feed f join fetch f.user where f.regionCode =:regionCode order by f.likes+f.comments.size desc")
    Slice<Feed> findAllFeedsHOT(@Param("regionCode") int reginCode, Pageable pageable);

    @Query("select f from Feed f where f.user.id = :userId order by f.id desc")
    List<Feed> findAllMyFeeds(@Param("userId") Long userId);

    @Query("select f from Feed f where f.regionCode =:regionCode order by f.likes+f.comments.size desc")
    List<Feed> findHotFeeds(@Param("regionCode") int regionCode, Pageable limit);

}
