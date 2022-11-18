package com.example.backend.service.feed;

import java.util.List;

import com.example.backend.domain.post.Feed;
import com.example.backend.dto.feed.FeedRequestDTO;

public interface FeedService {

    Feed getFeed(Long feedId);

    List<Feed> getFeeds(String search);

    void createFeed(FeedRequestDTO feedRequestDTO, Long userId);

    void deleteFeed(Long id, Long userId);

    Feed updateFeed(Long postId, FeedRequestDTO feedRequestDTO, Long userId);

}
