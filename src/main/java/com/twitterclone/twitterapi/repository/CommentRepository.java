package com.twitterclone.twitterapi.repository;

import com.twitterclone.twitterapi.entity.Comment;
import com.twitterclone.twitterapi.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTweetOrderByCreatedAtDesc(Tweet tweet);
    List<Comment> findByTweetId(Long tweetId);
    List<Comment> findByUserId(Long userId);
} 