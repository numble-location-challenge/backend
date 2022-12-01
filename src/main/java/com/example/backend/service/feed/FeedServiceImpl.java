package com.example.backend.service.feed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.domain.Like;
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
    @Transactional(readOnly = true)
    public Feed getFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_FEED));
        boolean isLiked = likesRepository.existsByUserIdAndPostId(userId, feedId);
        feed.setLiked(isLiked);
        return feed;
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<Feed> getFeeds(FeedSearch feedSearch, Long userId) {
        User user = userRepository.findReadOnlyById(userId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        int pageNum = (feedSearch.getPage() == 0) ? 0 : (feedSearch.getPage() - 1);
        Pageable pageable = PageRequest.of(pageNum, feedSearch.getSize());
        List<Like> likes = likesRepository.findByUserId(user.getId());
        if (feedSearch.getFilter().equals("LATEST")) {
            Slice<Feed> feeds = feedRepository.findAllFeedsLATEST(user.getRegionCodeFromDongCode(), pageable);
            checkLike(feeds, likes);
            return feeds;
        } else if (feedSearch.getFilter().equals("HOT")) {
            Slice<Feed> feeds = feedRepository.findAllFeedsHOT(user.getRegionCodeFromDongCode(), pageable);
            checkLike(feeds, likes);
            return feeds;
        } else {
            throw new FeedInvalidInputException(FeedInvalidInputExceptionType.INVALID_INPUT_FILTER);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feed> getMyFeeds(Long userId) {
        User user = userRepository.findReadOnlyById(userId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        List<Feed> feeds = feedRepository.findAllMyFeeds(user.getId());
        return feeds;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feed> getHotPreviewFeeds(Long userId) {
        User user = userRepository.findReadOnlyById(userId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        likesRepository.findByUserId(user.getId());
        int regionCode = user.getRegionCodeFromDongCode();
        Pageable limit = PageRequest.of(0, 10);
        List<Feed> hotFeeds = feedRepository.findHotFeeds(regionCode, limit);
        List<Like> likes = likesRepository.findByUserId(user.getId());
        checkLike(hotFeeds, likes);
        return hotFeeds;
    }

    @Override
    @Transactional
    public void createFeed(FeedRequestDTO feedRequestDTO, Long userId) {
        User user = userRepository.findReadOnlyById(userId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        Social social = null;
        if (feedRequestDTO.getSocialId() != null) {
            social = socialRepository.findById(feedRequestDTO.getSocialId())
                .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_SOCIAL));
        }
        List<PostImage> postImages = feedRequestDTO.getImages()
            .stream().map(images -> new PostImage(images.getImagePath()))
            .collect(Collectors.toList());
        Feed feed = Feed.createFeed(user, postImages, feedRequestDTO.getContents(),social);
        feedRepository.save(feed);
    }

    @Override
    @Transactional
    public void deleteFeed(Long postId, Long userId) {
        User user = userRepository.findReadOnlyById(userId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_USER));
        Feed feed = feedRepository.findById(postId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_FEED));
        if (hasPermission(feed, user.getId())) {
            commentRepository.deleteByPostId(feed.getId());
            likesRepository.deleteById(userId,postId);
            feedRepository.deleteById(feed.getId());
        } else {
            throw new ForbiddenException(ForbiddenExceptionType.NOT_AUTHORITY_DELETE_FEED);
        }
    }

    @Override
    @Transactional
    public Feed updateFeed(Long postId, FeedRequestDTO feedRequestDTO, Long userId) {
        Feed feed = feedRepository.findById(postId)
            .orElseThrow(() -> new EntityNotExistsException(EntityNotExistsExceptionType.NOT_FOUND_FEED));
        if (hasPermission(feed, userId)) {
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
            feed.updateFeed(feedRequestDTO.getContents(), social, postImages);
        } else {
            throw new ForbiddenException(ForbiddenExceptionType.NOT_AUTHORITY_UPDATE_FEED);
        }
        return feed;
    }

    private boolean hasPermission(Feed feed, Long userId) {
        if (feed.getUser().getId().equals(userId)) {
            return true;
        }
        return false;
    }

    private void checkLike(List<Feed> feeds, List<Like> likes) {
        for (Feed feed : feeds) {
            for (Like like : likes) {
                if (like.getPost().getId().equals(feed.getId())) {
                    feed.setLiked(true);
                    break;
                }
            }
        }
    }

    private void checkLike(Slice<Feed> feeds, List<Like> likes) {
        for (Feed feed : feeds) {
            for (Like like : likes) {
                if (like.getPost().getId().equals(feed.getId())) {
                    feed.setLiked(true);
                    break;
                }
            }
        }
    }
}
