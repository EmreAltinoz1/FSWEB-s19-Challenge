package com.twitterclone.twitterapi.controller;

import com.twitterclone.twitterapi.entity.Retweet;
import com.twitterclone.twitterapi.service.RetweetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetweetControllerTest {

    @Mock
    private RetweetService retweetService;

    @InjectMocks
    private RetweetController retweetController;

    private Long tweetId;
    private Long userId;
    private Retweet testRetweet;

    @BeforeEach
    void setUp() {
        tweetId = 1L;
        userId = 1L;
        testRetweet = new Retweet();
        testRetweet.setId(1L);
    }

    @Test
    void createRetweet_Success() {
        when(retweetService.createRetweet(tweetId)).thenReturn(testRetweet);

        ResponseEntity<Retweet> response = retweetController.createRetweet(tweetId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testRetweet, response.getBody());
    }

    @Test
    void deleteRetweet_Success() {
        doNothing().when(retweetService).deleteRetweet(tweetId);

        ResponseEntity<Void> response = retweetController.deleteRetweet(tweetId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getUserRetweets_Success() {
        List<Retweet> retweets = Arrays.asList(testRetweet);
        when(retweetService.getUserRetweets(userId)).thenReturn(retweets);

        ResponseEntity<List<Retweet>> response = retweetController.getUserRetweets(userId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(retweets, response.getBody());
    }

    @Test
    void hasUserRetweeted_Success() {
        when(retweetService.hasUserRetweeted(tweetId)).thenReturn(true);

        ResponseEntity<Boolean> response = retweetController.hasUserRetweeted(tweetId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
    }
} 