package com.example.backend.service.feed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.PostImage;
import com.example.backend.domain.User;
import com.example.backend.domain.post.Feed;
import com.example.backend.domain.post.Social;
import com.example.backend.dto.PostImageDTO;
import com.example.backend.dto.feed.FeedRequestDTO;
import com.example.backend.dto.feed.FeedSearch;
import com.example.backend.global.exception.EntityNotExistsException;
import com.example.backend.global.exception.EntityNotExistsExceptionType;
import com.example.backend.global.exception.ForbiddenException;
import com.example.backend.global.exception.ForbiddenExceptionType;
import com.example.backend.global.exception.feed.FeedInvalidInputException;
import com.example.backend.global.exception.feed.FeedInvalidInputExceptionType;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.FeedRepository;
import com.example.backend.repository.LikesRepository;
import com.example.backend.repository.PostImageRepository;
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
    private final CommentRepository commentRepository;
    private final PostImageRepository postImageRepository;
    private final LikesRepository likesRepository;

    @Override
    public Feed getFeed(Long feedId, String userEmail){
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_FEED));
        return feed;
    }

    @Transactional(readOnly = true)
    @BatchSize(size = 100)
    @Override
    public Slice<Feed> getFeeds(FeedSearch feedSearch, String userName){
        User user = userRepository.findByEmail(userName)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        int regionCode = user.getDongCode()/1000;
        log.info("getPage {} , getSize {}, cond {}", feedSearch.getPage(),feedSearch.getSize(),feedSearch.getFilter());
        int pageNum = (feedSearch.getPage() == 0) ? 0 : (feedSearch.getPage()-1);
        Pageable pageable = PageRequest.of(pageNum, feedSearch.getSize());

        if (feedSearch.getFilter().equals("LATEST")) {
            log.info("최신순");
            Slice<Feed> feeds = feedRepository.findAllFeedsLATEST(regionCode, pageable);
            return feeds;
        } else if (feedSearch.getFilter().equals("HOT")){
            log.info("인기순");
            Slice<Feed> feeds = feedRepository.findAllFeedsHOT(regionCode, pageable);
            return feeds;
        } else {
            throw new FeedInvalidInputException(FeedInvalidInputExceptionType.INVALID_INPUT_FILTER);
        }
    }

    @Override
    public List<Feed> getMyFeeds(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        List<Feed> feeds = feedRepository.findAllMyFeeds(user.getId());
        return feeds;
    }
    @Override
    public List<Feed> getHotPreviewFeeds(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        int regionCode = user.getDongCode()/1000;
        Pageable limit = PageRequest.of(0,10);
        List<Feed> hotFeeds = feedRepository.findHotFeeds(regionCode,limit);
        return hotFeeds;
    }

    @Override
    @Transactional
    public void createFeed(FeedRequestDTO feedRequestDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Social social = null;
        if (feedRequestDTO.getSocialId() != null){
            social = socialRepository.findById(feedRequestDTO.getSocialId()).orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));
    }
        List<PostImage> postImages = feedRequestDTO.getImages()
            .stream().map(images -> new PostImage(images.getImagePath()))
            .collect(Collectors.toList());

        Feed feed = Feed.createFeed(user, postImages, feedRequestDTO.getContents(),
            feedRequestDTO.getRegion(), social);

        feedRepository.save(feed);
    }

    @Override
    @Transactional
    public void deleteFeed(Long postId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        Feed feed = feedRepository.findById(postId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_FEED));
        if (hasPermission(feed,user.getEmail())){
            commentRepository.deleteByPostId(feed.getId());
            feedRepository.deleteById(feed.getId());
        } else {
            throw new ForbiddenException(ForbiddenExceptionType.NOT_AUTHORITY_DELETE_FEED);
        }
    }

    @Override
    @Transactional
    public Feed updateFeed(Long postId, FeedRequestDTO feedRequestDTO, String userEmail) {
        Feed feed = feedRepository.findById(postId).orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_FEED));
        if (hasPermission(feed,userEmail)) {
            Social social = null;
            if (feedRequestDTO.getSocialId() != null) {
                social = socialRepository.findById(feedRequestDTO.getSocialId())
                    .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));
            }
            List<PostImage> postImages = null;
            if (!feedRequestDTO.getImages().isEmpty()) {
                postImageRepository.deleteAllByPostId(feed.getId());
                postImages = new ArrayList<>();
                for (PostImageDTO imageDTO : feedRequestDTO.getImages()) {
                    postImages.add(new PostImage(imageDTO.getImagePath()));
                }
            }
            feed.updateFeed(feedRequestDTO.getContents(), social, postImages, feedRequestDTO.getRegion());
        } else {
            throw new ForbiddenException(ForbiddenExceptionType.NOT_AUTHORITY_UPDATE_FEED);
        }
        return feed;
    }

    @Override
    public boolean checkLike(Long postId, String userEmail){
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        return likesRepository.existsByUserIdAndPostId(user.getId(), postId);
    }

    private boolean hasPermission(Feed feed, String userEmail) {
        if (feed.getUser().getEmail().equals(userEmail)) {
            return true;
        } else {
            return false;
        }
    }
}
