package com.twitterclone.twitterapi.controller;

import com.twitterclone.twitterapi.entity.Comment;
import com.twitterclone.twitterapi.service.CommentService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private Comment testComment;
    private Long tweetId;
    private String testContent;

    @BeforeEach
    void setUp() {
        testComment = new Comment();
        testComment.setId(1L);
        testComment.setContent("Test Comment");
        tweetId = 1L;
        testContent = "Test Comment";
    }

    @Test
    void createComment_Success() {
        when(commentService.createComment(tweetId, testContent))
                .thenReturn(testComment);

        ResponseEntity<Comment> response = commentController.createComment(tweetId, testContent);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testComment, response.getBody());
        verify(commentService).createComment(tweetId, testContent);
    }

    @Test
    void updateComment_Success() {
        String updatedContent = "Updated Comment";
        when(commentService.updateComment(testComment.getId(), updatedContent))
                .thenReturn(testComment);

        ResponseEntity<Comment> response = commentController.updateComment(testComment.getId(), updatedContent);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testComment, response.getBody());
        verify(commentService).updateComment(testComment.getId(), updatedContent);
    }

    @Test
    void deleteComment_Success() {
        doNothing().when(commentService).deleteComment(testComment.getId());

        ResponseEntity<Void> response = commentController.deleteComment(testComment.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(commentService).deleteComment(testComment.getId());
    }

    @Test
    void getCommentsByTweetId_Success() {
        List<Comment> comments = Arrays.asList(testComment);
        when(commentService.getCommentsByTweetId(tweetId))
                .thenReturn(comments);

        ResponseEntity<List<Comment>> response = commentController.getCommentsByTweetId(tweetId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(comments, response.getBody());
        verify(commentService).getCommentsByTweetId(tweetId);
    }
} 