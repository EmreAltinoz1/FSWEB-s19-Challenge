package com.twitterclone.twitterapi.service.impl;

import com.twitterclone.twitterapi.entity.Tweet;
import com.twitterclone.twitterapi.entity.User;
import com.twitterclone.twitterapi.repository.TweetRepository;
import com.twitterclone.twitterapi.repository.UserRepository;
import com.twitterclone.twitterapi.service.TweetService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Tweet createTweet(Tweet tweet, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        tweet.setUser(user);
        return tweetRepository.save(tweet);
    }

    @Override
    public List<Tweet> findTweetsByUserId(Long userId) {
        return tweetRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Tweet findTweetById(Long id) {
        return tweetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tweet not found"));
    }

    @Override
    @Transactional
    public Tweet updateTweet(Long id, Tweet tweetDetails) {
        Tweet tweet = findTweetById(id);
        tweet.setContent(tweetDetails.getContent());
        return tweetRepository.save(tweet);
    }

    @Override
    @Transactional
    public void deleteTweet(Long id, Long userId) {
        Tweet tweet = findTweetById(id);
        if (!tweet.getUser().getId().equals(userId)) {
            throw new IllegalStateException("You can only delete your own tweets");
        }
        tweetRepository.delete(tweet);
    }
} 