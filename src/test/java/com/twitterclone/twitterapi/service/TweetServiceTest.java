package com.twitterclone.twitterapi.service;

import com.twitterclone.twitterapi.entity.Tweet;
import com.twitterclone.twitterapi.entity.User;
import com.twitterclone.twitterapi.repository.TweetRepository;
import com.twitterclone.twitterapi.repository.UserRepository;
import com.twitterclone.twitterapi.service.impl.TweetServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    private TweetService tweetService;

    @BeforeEach
    void setUp() {
        tweetService = new TweetServiceImpl(tweetRepository, userRepository);
    }

    @Test
    void createTweet_WhenUserExists_ReturnsSavedTweet() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Tweet tweet = new Tweet();
        tweet.setContent("Test tweet");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);

        // Act
        Tweet savedTweet = tweetService.createTweet(tweet, 1L);

        // Assert
        assertNotNull(savedTweet);
        assertEquals("Test tweet", savedTweet.getContent());
        assertEquals(user, savedTweet.getUser());
        verify(tweetRepository).save(tweet);
    }

    @Test
    void createTweet_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        Tweet tweet = new Tweet();
        tweet.setContent("Test tweet");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            tweetService.createTweet(tweet, 1L);
        });
        verify(tweetRepository, never()).save(any());
    }

    @Test
    void findTweetsByUserId_ReturnsListOfTweets() {
        // Arrange
        Tweet tweet1 = new Tweet();
        tweet1.setContent("Tweet 1");
        Tweet tweet2 = new Tweet();
        tweet2.setContent("Tweet 2");
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);

        when(tweetRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(tweets);

        // Act
        List<Tweet> foundTweets = tweetService.findTweetsByUserId(1L);

        // Assert
        assertEquals(2, foundTweets.size());
        assertEquals("Tweet 1", foundTweets.get(0).getContent());
        assertEquals("Tweet 2", foundTweets.get(1).getContent());
    }

    @Test
    void findTweetById_WhenTweetExists_ReturnsTweet() {
        // Arrange
        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setContent("Test tweet");

        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        // Act
        Tweet foundTweet = tweetService.findTweetById(1L);

        // Assert
        assertNotNull(foundTweet);
        assertEquals("Test tweet", foundTweet.getContent());
    }

    @Test
    void findTweetById_WhenTweetDoesNotExist_ThrowsException() {
        // Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            tweetService.findTweetById(1L);
        });
    }

    @Test
    void updateTweet_WhenTweetExists_ReturnsUpdatedTweet() {
        // Arrange
        Tweet existingTweet = new Tweet();
        existingTweet.setId(1L);
        existingTweet.setContent("Original content");

        Tweet updatedTweet = new Tweet();
        updatedTweet.setContent("Updated content");

        when(tweetRepository.findById(1L)).thenReturn(Optional.of(existingTweet));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(existingTweet);

        // Act
        Tweet result = tweetService.updateTweet(1L, updatedTweet);

        // Assert
        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
        verify(tweetRepository).save(existingTweet);
    }

    @Test
    void deleteTweet_WhenUserIsOwner_DeletesTweet() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setUser(user);

        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        // Act
        tweetService.deleteTweet(1L, 1L);

        // Assert
        verify(tweetRepository).delete(tweet);
    }

    @Test
    void deleteTweet_WhenUserIsNotOwner_ThrowsException() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setUser(user);

        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            tweetService.deleteTweet(1L, 2L);
        });
        verify(tweetRepository, never()).delete(any());
    }
} 