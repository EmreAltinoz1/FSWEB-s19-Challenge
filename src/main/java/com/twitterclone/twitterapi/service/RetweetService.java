package com.twitterclone.twitterapi.service;

import com.twitterclone.twitterapi.entity.Retweet;
import java.util.List;

public interface RetweetService {
    Retweet createRetweet(Long tweetId);
    void deleteRetweet(Long tweetId);
    List<Retweet> getUserRetweets(Long userId);
    boolean hasUserRetweeted(Long tweetId);
} 