package com.twitterclone.twitterapi.controller;

import com.twitterclone.twitterapi.entity.Retweet;
import com.twitterclone.twitterapi.service.RetweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retweets")
@CrossOrigin(origins = "http://localhost:3200")
public class RetweetController {

    @Autowired
    private RetweetService retweetService;

    @PostMapping("/{tweetId}")
    public ResponseEntity<Retweet> createRetweet(@PathVariable Long tweetId) {
        Retweet retweet = retweetService.createRetweet(tweetId);
        return ResponseEntity.ok(retweet);
    }

    @DeleteMapping("/{tweetId}")
    public ResponseEntity<Void> deleteRetweet(@PathVariable Long tweetId) {
        retweetService.deleteRetweet(tweetId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Retweet>> getUserRetweets(@PathVariable Long userId) {
        List<Retweet> retweets = retweetService.getUserRetweets(userId);
        return ResponseEntity.ok(retweets);
    }

    @GetMapping("/check/{tweetId}")
    public ResponseEntity<Boolean> hasUserRetweeted(@PathVariable Long tweetId) {
        boolean hasRetweeted = retweetService.hasUserRetweeted(tweetId);
        return ResponseEntity.ok(hasRetweeted);
    }
} 