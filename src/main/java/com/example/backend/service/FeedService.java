package com.example.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.domain.PostImage;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Feed;
import com.example.backend.domain.post.Social;
import com.example.backend.dto.FeedRequestDTO;
import com.example.backend.dto.PostImageDTO;
import com.example.backend.repository.FeedRepository;
import com.example.backend.repository.PostImageRepository;
import com.example.backend.repository.SocialRepository;
import com.example.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final SocialRepository socialRepository;
    private final PostImageRepository imageRepository;

    public Feed getFeed(Long feedId){
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new IllegalArgumentException());
        return feed;
    }
    public List<Feed> getFeeds(String search){
        List<Feed> feeds = feedRepository.findAll();
        return feeds;

    }

    public void createFeed(FeedRequestDTO dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Social social = socialRepository.findById(dto.getSocialId()).orElseThrow();
        List<PostImage> postImages = new ArrayList<>();
        for (PostImageDTO imageDTO : dto.getImages()){
            postImages.add(new PostImage(imageDTO.getImagePath()));
        }
        imageRepository.saveAll(postImages);
        Feed feed = Feed.builder()
            .contents(dto.getContents())
            .user(user)
            .likes(0)
            .social(social)
            .images(postImages)
            .region(dto.getRegion())
            .build();
        feedRepository.save(feed);
    }

    public void deleteFeed(Long id, Long userId) {
        feedRepository.deleteById(id);
    }

    public Feed updateFeed(Long postId, FeedRequestDTO feedRequestDTO, Long userId) {
        Feed feed = feedRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow(); // 권한 확인용
        Social social = socialRepository.findById(feedRequestDTO.getSocialId()).orElseThrow();

        List<PostImage> postImages = new ArrayList<>();
        for (PostImageDTO imageDTO : feedRequestDTO.getImages()){
            postImages.add(new PostImage(imageDTO.getImagePath()));
        }

        feed.updateFeed(feedRequestDTO.getContents(), social,postImages,feedRequestDTO.getRegion());
        return feedRepository.save(feed);
    }
}
