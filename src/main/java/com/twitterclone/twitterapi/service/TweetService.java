package com.twitterclone.twitterapi.service;

import com.twitterclone.twitterapi.entity.Tweet;

import java.util.List;

public interface TweetService {
    Tweet createTweet(Tweet tweet, Long userId);
    List<Tweet> findTweetsByUserId(Long userId);
    Tweet findTweetById(Long id);
    Tweet updateTweet(Long id, Tweet tweet);
    void deleteTweet(Long id, Long userId);
} 