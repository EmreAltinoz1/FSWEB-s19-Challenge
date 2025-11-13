package com.twitterclone.twitterapi.service;

import com.twitterclone.twitterapi.entity.Like;
import com.twitterclone.twitterapi.entity.Tweet;
import com.twitterclone.twitterapi.entity.User;
import com.twitterclone.twitterapi.repository.LikeRepository;
import com.twitterclone.twitterapi.repository.TweetRepository;
import com.twitterclone.twitterapi.repository.UserRepository;
import com.twitterclone.twitterapi.service.impl.LikeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TweetRepository tweetRepository;

    private LikeService likeService;

    @BeforeEach
    void setUp() {
        likeService = new LikeServiceImpl(likeRepository, userRepository, tweetRepository);
    }

    @Test
    void likeTweet_WhenUserAndTweetExistAndNotLiked_ReturnsSavedLike() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setContent("Test tweet");

        Like like = new Like();
        like.setUser(user);
        like.setTweet(tweet);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(tweet));
        when(likeRepository.existsByUserIdAndTweetId(1L, 1L)).thenReturn(false);
        when(likeRepository.save(any(Like.class))).thenReturn(like);

        // Act
        Like savedLike = likeService.likeTweet(1L, 1L);

        // Assert
        assertNotNull(savedLike);
        assertEquals(user, savedLike.getUser());
        assertEquals(tweet, savedLike.getTweet());
        verify(likeRepository).save(any(Like.class));
    }

    @Test
    void likeTweet_WhenTweetAlreadyLiked_ThrowsException() {
        // Arrange
        when(likeRepository.existsByUserIdAndTweetId(1L, 1L)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            likeService.likeTweet(1L, 1L);
        });
        verify(likeRepository, never()).save(any());
    }

    @Test
    void likeTweet_WhenUserDoesNotExist_ThrowsException() {
        // Arrange
        when(likeRepository.existsByUserIdAndTweetId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            likeService.likeTweet(1L, 1L);
        });
        verify(likeRepository, never()).save(any());
    }

    @Test
    void likeTweet_WhenTweetDoesNotExist_ThrowsException() {
        // Arrange
        User user = new User();
        user.setId(1L);

        when(likeRepository.existsByUserIdAndTweetId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(tweetRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            likeService.likeTweet(1L, 1L);
        });
        verify(likeRepository, never()).save(any());
    }

    @Test
    void unlikeTweet_WhenLikeExists_DeletesLike() {
        // Arrange
        when(likeRepository.existsByUserIdAndTweetId(1L, 1L)).thenReturn(true);

        // Act
        likeService.unlikeTweet(1L, 1L);

        // Assert
        verify(likeRepository).deleteByUserIdAndTweetId(1L, 1L);
    }

    @Test
    void unlikeTweet_WhenLikeDoesNotExist_ThrowsException() {
        // Arrange
        when(likeRepository.existsByUserIdAndTweetId(1L, 1L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            likeService.unlikeTweet(1L, 1L);
        });
        verify(likeRepository, never()).deleteByUserIdAndTweetId(anyLong(), anyLong());
    }
} 