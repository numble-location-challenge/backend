package com.example.backend.service.feed;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.example.backend.domain.post.Feed;
import com.example.backend.dto.feed.FeedRequestDTO;
import com.example.backend.dto.feed.FeedSearch;

public interface FeedService {

    Feed getFeed(Long feedId, String userEmail);

    Slice<Feed> getFeeds(FeedSearch feedSearch, String userEmail);

    List<Feed> getHotPreviewFeeds(String userEmail);

    void createFeed(FeedRequestDTO feedRequestDTO, String userEmail);

    void deleteFeed(Long id, String userEmail);

    Feed updateFeed(Long postId, FeedRequestDTO feedRequestDTO, String userEmail);

    List<Feed> getMyFeeds(String userEmail);

    boolean checkLike(Long PostId, String userEmail);
}
