package com.example.backend.service.feed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.PostImage;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Feed;
import com.example.backend.domain.post.Social;
import com.example.backend.dto.PostImageDTO;
import com.example.backend.dto.feed.FeedRequestDTO;
import com.example.backend.repository.FeedRepository;
import com.example.backend.repository.SocialRepository;
import com.example.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final SocialRepository socialRepository;

    @Override
    public Feed getFeed(Long feedId){
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new IllegalArgumentException());
        return feed;
    }

    @Override
    public List<Feed> getFeeds(String search){
        List<Feed> feeds = feedRepository.findAll();
        return feeds;
    }

    @Override
    @Transactional
    public void createFeed(FeedRequestDTO feedRequestDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Social social = null;
        if (feedRequestDTO.getSocialId() != null){
            social = socialRepository.findById(feedRequestDTO.getSocialId()).orElseThrow();
    }

        List<PostImage> postImages = feedRequestDTO.getImages()
            .stream().map(images -> new PostImage(images.getImagePath()))
            .collect(Collectors.toList());

        Feed feed = Feed.createFeed(user, postImages, feedRequestDTO.getContents(),
            feedRequestDTO.getRegion(), social);

        feedRepository.save(feed);
    }

    @Override
    public void deleteFeed(Long id, Long userId) {
        feedRepository.deleteById(id);
    }


    //TODO 피드 수정기능 구현
    @Override
    @Transactional
    public Feed updateFeed(Long postId, FeedRequestDTO feedRequestDTO, Long userId) {
        Feed feed = feedRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow(); // 권한 확인용
        Social social = socialRepository.findById(feedRequestDTO.getSocialId()).orElseThrow();

        List<PostImage> postImages = new ArrayList<>();
        for (PostImageDTO imageDTO : feedRequestDTO.getImages()){
            postImages.add(new PostImage(imageDTO.getImagePath()));
        }

        feed.updateFeed(feedRequestDTO.getContents(), social,postImages,feedRequestDTO.getRegion());
        return feed;
    }
}
