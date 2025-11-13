package com.twitterclone.twitterapi.repository;

import com.twitterclone.twitterapi.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByTweetId(Long tweetId);
    List<Like> findByUserId(Long userId);
    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
    void deleteByUserIdAndTweetId(Long userId, Long tweetId);
} 