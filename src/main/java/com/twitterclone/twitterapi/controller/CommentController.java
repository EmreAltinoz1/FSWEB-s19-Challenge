package com.twitterclone.twitterapi.controller;

import com.twitterclone.twitterapi.entity.Comment;
import com.twitterclone.twitterapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:3200")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @RequestParam Long tweetId,
            @RequestParam String content) {
        Comment comment = commentService.createComment(tweetId, content);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long commentId,
            @RequestParam String content) {
        Comment comment = commentService.updateComment(commentId, content);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tweet/{tweetId}")
    public ResponseEntity<List<Comment>> getCommentsByTweetId(@PathVariable Long tweetId) {
        List<Comment> comments = commentService.getCommentsByTweetId(tweetId);
        return ResponseEntity.ok(comments);
    }
} 