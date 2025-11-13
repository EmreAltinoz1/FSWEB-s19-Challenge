package com.twitterclone.twitterapi.repository;

import com.twitterclone.twitterapi.entity.Retweet;
import com.twitterclone.twitterapi.entity.Tweet;
import com.twitterclone.twitterapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    List<Retweet> findByUserId(Long userId);
    List<Retweet> findByTweetId(Long tweetId);
    Optional<Retweet> findByUserIdAndTweetId(Long userId, Long tweetId);
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
    void deleteByUserIdAndTweetId(Long userId, Long tweetId);
    Optional<Retweet> findByUserAndTweet(User user, Tweet tweet);
    List<Retweet> findByUserOrderByCreatedAtDesc(User user);
    List<Retweet> findByTweetOrderByCreatedAtDesc(Tweet tweet);
} 