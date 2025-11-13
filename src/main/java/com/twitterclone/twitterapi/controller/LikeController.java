package com.twitterclone.twitterapi.controller;

import com.twitterclone.twitterapi.entity.Like;
import com.twitterclone.twitterapi.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{tweetId}/user/{userId}")
    public ResponseEntity<Like> likeTweet(@PathVariable Long tweetId,
                                        @PathVariable Long userId) {
        Like like = likeService.likeTweet(userId, tweetId);
        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }

    @DeleteMapping("/{tweetId}/user/{userId}")
    public ResponseEntity<Void> unlikeTweet(@PathVariable Long tweetId,
                                          @PathVariable Long userId) {
        likeService.unlikeTweet(userId, tweetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{tweetId}/user/{userId}")
    public ResponseEntity<Boolean> isLikedByUser(@PathVariable Long tweetId,
                                               @PathVariable Long userId) {
        return ResponseEntity.ok(likeService.isLikedByUser(userId, tweetId));
    }
} 