package com.twitterclone.twitterapi.service.impl;

import com.twitterclone.twitterapi.entity.Like;
import com.twitterclone.twitterapi.entity.Tweet;
import com.twitterclone.twitterapi.entity.User;
import com.twitterclone.twitterapi.repository.LikeRepository;
import com.twitterclone.twitterapi.repository.TweetRepository;
import com.twitterclone.twitterapi.repository.UserRepository;
import com.twitterclone.twitterapi.service.LikeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Override
    @Transactional
    public Like likeTweet(Long userId, Long tweetId) {
        if (likeRepository.existsByUserIdAndTweetId(userId, tweetId)) {
            throw new IllegalStateException("Tweet is already liked by the user");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new EntityNotFoundException("Tweet not found"));

        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);
        return likeRepository.save(like);
    }

    @Override
    @Transactional
    public void unlikeTweet(Long userId, Long tweetId) {
        if (!likeRepository.existsByUserIdAndTweetId(userId, tweetId)) {
            throw new IllegalStateException("Tweet is not liked by the user");
        }

        likeRepository.deleteByUserIdAndTweetId(userId, tweetId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLikedByUser(Long userId, Long tweetId) {
        return likeRepository.existsByUserIdAndTweetId(userId, tweetId);
    }
} 