package com.twitterclone.twitterapi.service;

import com.twitterclone.twitterapi.entity.Retweet;
import com.twitterclone.twitterapi.entity.Tweet;
import com.twitterclone.twitterapi.entity.User;
import com.twitterclone.twitterapi.exception.ResourceNotFoundException;
import com.twitterclone.twitterapi.repository.RetweetRepository;
import com.twitterclone.twitterapi.repository.TweetRepository;
import com.twitterclone.twitterapi.repository.UserRepository;
import com.twitterclone.twitterapi.service.impl.RetweetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RetweetServiceTest {

    @Mock
    private RetweetRepository retweetRepository;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private RetweetServiceImpl retweetService;

    private User testUser;
    private Tweet testTweet;
    private Retweet testRetweet;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testTweet = new Tweet();
        testTweet.setId(1L);
        testTweet.setContent("Test Tweet");
        testTweet.setUser(testUser);

        testRetweet = new Retweet();
        testRetweet.setId(1L);
        testRetweet.setUser(testUser);
        testRetweet.setTweet(testTweet);

        // Setup security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");
    }

    @Test
    void createRetweet_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));
        when(retweetRepository.findByUserAndTweet(testUser, testTweet)).thenReturn(Optional.empty());
        when(retweetRepository.save(any(Retweet.class))).thenReturn(testRetweet);

        Retweet result = retweetService.createRetweet(1L);

        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals(testTweet, result.getTweet());
        verify(retweetRepository).save(any(Retweet.class));
    }

    @Test
    void createRetweet_AlreadyRetweeted() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));
        when(retweetRepository.findByUserAndTweet(testUser, testTweet)).thenReturn(Optional.of(testRetweet));

        assertThrows(IllegalStateException.class, () -> retweetService.createRetweet(1L));
        verify(retweetRepository, never()).save(any(Retweet.class));
    }

    @Test
    void deleteRetweet_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));
        when(retweetRepository.findByUserAndTweet(testUser, testTweet)).thenReturn(Optional.of(testRetweet));

        retweetService.deleteRetweet(1L);

        verify(retweetRepository).delete(testRetweet);
    }

    @Test
    void getUserRetweets_Success() {
        List<Retweet> expectedRetweets = Arrays.asList(testRetweet);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(retweetRepository.findByUserOrderByCreatedAtDesc(testUser)).thenReturn(expectedRetweets);

        List<Retweet> result = retweetService.getUserRetweets(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testRetweet, result.get(0));
    }

    @Test
    void hasUserRetweeted_True() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));
        when(retweetRepository.findByUserAndTweet(testUser, testTweet)).thenReturn(Optional.of(testRetweet));

        boolean result = retweetService.hasUserRetweeted(1L);

        assertTrue(result);
    }

    @Test
    void hasUserRetweeted_False() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));
        when(retweetRepository.findByUserAndTweet(testUser, testTweet)).thenReturn(Optional.empty());

        boolean result = retweetService.hasUserRetweeted(1L);

        assertFalse(result);
    }
} 