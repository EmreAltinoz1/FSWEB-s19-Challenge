package com.twitterclone.twitterapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitterclone.twitterapi.config.TestSecurityConfig;
import com.twitterclone.twitterapi.config.TestWebSecurityConfig;
import com.twitterclone.twitterapi.entity.Like;
import com.twitterclone.twitterapi.entity.Tweet;
import com.twitterclone.twitterapi.entity.User;
import com.twitterclone.twitterapi.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikeController.class)
@Import({TestSecurityConfig.class, TestWebSecurityConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LikeService likeService;

    private Long userId;
    private Long tweetId;
    private Like testLike;
    private User testUser;
    private Tweet testTweet;

    @BeforeEach
    void setUp() {
        userId = 1L;
        tweetId = 1L;
        
        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername("testUser");
        
        testTweet = new Tweet();
        testTweet.setId(tweetId);
        testTweet.setContent("Test Tweet");
        testTweet.setUser(testUser);
        
        testLike = new Like();
        testLike.setId(1L);
        testLike.setUser(testUser);
        testLike.setTweet(testTweet);
    }

    @Test
    void likeTweet_ShouldReturnCreatedLike() throws Exception {
        when(likeService.likeTweet(eq(userId), eq(tweetId)))
                .thenReturn(testLike);

        mockMvc.perform(post("/api/likes/{tweetId}/user/{userId}", tweetId, userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(testLike)));
    }

    @Test
    void unlikeTweet_ShouldReturnNoContent() throws Exception {
        doNothing().when(likeService).unlikeTweet(eq(tweetId), eq(userId));

        mockMvc.perform(delete("/api/likes/{tweetId}/user/{userId}", tweetId, userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void isLikedByUser_ShouldReturnBoolean() throws Exception {
        when(likeService.isLikedByUser(eq(tweetId), eq(userId)))
                .thenReturn(true);

        mockMvc.perform(get("/api/likes/{tweetId}/user/{userId}", tweetId, userId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
} 