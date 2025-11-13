package com.twitterclone.twitterapi.service;

import com.twitterclone.twitterapi.entity.Comment;
import java.util.List;

public interface CommentService {
    Comment createComment(Long tweetId, String content);
    Comment updateComment(Long commentId, String content);
    void deleteComment(Long commentId);
    List<Comment> getCommentsByTweetId(Long tweetId);
} 