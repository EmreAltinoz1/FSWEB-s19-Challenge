package com.twitterclone.twitterapi.service;

import com.twitterclone.twitterapi.entity.Like;

public interface LikeService {
    Like likeTweet(Long userId, Long tweetId);
    void unlikeTweet(Long userId, Long tweetId);
    boolean isLikedByUser(Long userId, Long tweetId);
} 