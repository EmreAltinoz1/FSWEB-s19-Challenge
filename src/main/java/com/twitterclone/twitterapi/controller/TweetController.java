package com.twitterclone.twitterapi.controller;

import com.twitterclone.twitterapi.entity.Tweet;
import com.twitterclone.twitterapi.service.TweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tweets")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<Tweet> createTweet(@Valid @RequestBody Tweet tweet, @PathVariable Long userId) {
        Tweet createdTweet = tweetService.createTweet(tweet, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTweet);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Tweet>> findTweetsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(tweetService.findTweetsByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tweet> findTweetById(@PathVariable Long id) {
        return ResponseEntity.ok(tweetService.findTweetById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable Long id, @Valid @RequestBody Tweet tweet) {
        return ResponseEntity.ok(tweetService.updateTweet(id, tweet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id, @RequestParam Long userId) {
        tweetService.deleteTweet(id, userId);
        return ResponseEntity.noContent().build();
    }
} 