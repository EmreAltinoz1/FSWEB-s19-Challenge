package com.twitterclone.twitterapi.service;

import com.twitterclone.twitterapi.entity.Comment;
import com.twitterclone.twitterapi.entity.Tweet;
import com.twitterclone.twitterapi.entity.User;
import com.twitterclone.twitterapi.exception.ResourceNotFoundException;
import com.twitterclone.twitterapi.repository.CommentRepository;
import com.twitterclone.twitterapi.repository.TweetRepository;
import com.twitterclone.twitterapi.repository.UserRepository;
import com.twitterclone.twitterapi.service.impl.CommentServiceImpl;
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
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User testUser;
    private Tweet testTweet;
    private Comment testComment;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testTweet = new Tweet();
        testTweet.setId(1L);
        testTweet.setContent("Test Tweet");
        testTweet.setUser(testUser);

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setContent("Test Comment");
        testComment.setUser(testUser);
        testComment.setTweet(testTweet);

        // Setup security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");
    }

    @Test
    void createComment_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        Comment result = commentService.createComment(1L, "Test Comment");

        assertNotNull(result);
        assertEquals("Test Comment", result.getContent());
        assertEquals(testUser, result.getUser());
        assertEquals(testTweet, result.getTweet());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void updateComment_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        Comment result = commentService.updateComment(1L, "Updated Comment");

        assertNotNull(result);
        assertEquals("Updated Comment", result.getContent());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void updateComment_NotOwner() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("otherUser");
        testComment.setUser(otherUser);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        assertThrows(IllegalStateException.class, () -> 
            commentService.updateComment(1L, "Updated Comment")
        );
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void deleteComment_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        commentService.deleteComment(1L);

        verify(commentRepository).delete(testComment);
    }

    @Test
    void deleteComment_NotOwnerOrTweetOwner() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("otherUser");

        testComment.setUser(otherUser);
        testTweet.setUser(otherUser);
        
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        assertThrows(IllegalStateException.class, () -> 
            commentService.deleteComment(1L)
        );
        verify(commentRepository, never()).delete(any(Comment.class));
    }

    @Test
    void getCommentsByTweetId_Success() {
        List<Comment> expectedComments = Arrays.asList(testComment);
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));
        when(commentRepository.findByTweetOrderByCreatedAtDesc(testTweet)).thenReturn(expectedComments);

        List<Comment> result = commentService.getCommentsByTweetId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testComment, result.get(0));
    }

    @Test
    void getCommentsByTweetId_TweetNotFound() {
        when(tweetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            commentService.getCommentsByTweetId(1L)
        );
    }
} 