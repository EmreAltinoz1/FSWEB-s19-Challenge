package com.twitterclone.twitterapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitterclone.twitterapi.config.TestSecurityConfig;
import com.twitterclone.twitterapi.config.TestWebSecurityConfig;
import com.twitterclone.twitterapi.entity.Tweet;
import com.twitterclone.twitterapi.service.TweetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TweetController.class)
@Import({TestSecurityConfig.class, TestWebSecurityConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TweetService tweetService;

    private Tweet testTweet;
    private Long userId;

    @BeforeEach
    void setUp() {
        testTweet = new Tweet();
        testTweet.setId(1L);
        testTweet.setContent("Test Tweet");
        userId = 1L;
    }

    @Test
    void createTweet_ShouldReturnCreatedTweet() throws Exception {
        when(tweetService.createTweet(any(Tweet.class), eq(userId)))
                .thenReturn(testTweet);

        mockMvc.perform(post("/api/tweets/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTweet)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(testTweet)));
    }

    @Test
    void findTweetsByUserId_ShouldReturnListOfTweets() throws Exception {
        List<Tweet> tweets = Arrays.asList(testTweet);
        when(tweetService.findTweetsByUserId(userId))
                .thenReturn(tweets);

        mockMvc.perform(get("/api/tweets/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tweets)));
    }

    @Test
    void findTweetById_ShouldReturnTweet() throws Exception {
        when(tweetService.findTweetById(testTweet.getId()))
                .thenReturn(testTweet);

        mockMvc.perform(get("/api/tweets/{id}", testTweet.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testTweet)));
    }

    @Test
    void updateTweet_ShouldReturnUpdatedTweet() throws Exception {
        Tweet updatedTweet = new Tweet();
        updatedTweet.setId(testTweet.getId());
        updatedTweet.setContent("Updated Tweet");

        when(tweetService.updateTweet(eq(testTweet.getId()), any(Tweet.class)))
                .thenReturn(updatedTweet);

        mockMvc.perform(put("/api/tweets/{id}", testTweet.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTweet)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedTweet)));
    }
} 