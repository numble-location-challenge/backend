package com.example.backend.service.feed;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.example.backend.domain.post.Feed;
import com.example.backend.dto.feed.FeedRequestDTO;
import com.example.backend.dto.feed.FeedSearch;
import com.example.backend.dto.feed.FeedUpdateRequestDTO;

public interface FeedService {

    Feed getFeed(Long feedId, Long userId);

    Slice<Feed> getFeeds(FeedSearch feedSearch, Long userId);

    List<Feed> getHotPreviewFeeds(Long userId);

    void createFeed(FeedRequestDTO feedRequestDTO, Long userId);

    void deleteFeed(Long id, Long userId);

    Feed updateFeed(Long postId, FeedUpdateRequestDTO feedUpdateRequestDTO, Long userId);

    List<Feed> getMyFeeds(Long userId);

}
