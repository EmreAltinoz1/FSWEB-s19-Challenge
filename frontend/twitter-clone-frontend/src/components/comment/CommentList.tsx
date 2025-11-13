import React, { useState, useEffect } from 'react';
import {
  Box,
  TextField,
  Button,
  Typography,
  Avatar,
  IconButton,
  CircularProgress,
  Alert,
} from '@mui/material';
import { Delete as DeleteIcon } from '@mui/icons-material';
import commentService, { Comment } from '../../services/commentService';
import authService from '../../services/authService';

interface CommentListProps {
  tweetId: string;
}

const CommentList: React.FC<CommentListProps> = ({ tweetId }) => {
  const [comments, setComments] = useState<Comment[]>([]);
  const [newComment, setNewComment] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const currentUser = authService.getCurrentUser();
  const isAuthenticated = authService.isAuthenticated();

  const fetchComments = async () => {
    try {
      const fetchedComments = await commentService.getCommentsByTweetId(tweetId);
      setComments(fetchedComments);
    } catch (err: any) {
      console.error('Yorumlar yüklenirken hata oluştu:', err);
    }
  };

  useEffect(() => {
    fetchComments();
  }, [tweetId]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newComment.trim()) return;

    setLoading(true);
    setError(null);

    try {
      await commentService.createComment(tweetId, newComment.trim());
      setNewComment('');
      fetchComments();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Yorum eklenirken bir hata oluştu');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (commentId: string) => {
    try {
      await commentService.deleteComment(commentId);
      fetchComments();
    } catch (err: any) {
      console.error('Yorum silinirken hata oluştu:', err);
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('tr-TR', {
      day: 'numeric',
      month: 'long',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date);
  };

  return (
    <Box sx={{ mt: 2 }}>
      {isAuthenticated && (
        <form onSubmit={handleSubmit}>
          <TextField
            fullWidth
            multiline
            rows={2}
            placeholder="Yorumunuzu yazın..."
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            disabled={loading}
          />
          {error && (
            <Alert severity="error" sx={{ mt: 1 }}>
              {error}
            </Alert>
          )}
          <Button
            type="submit"
            variant="contained"
            sx={{ mt: 1 }}
            disabled={loading || !newComment.trim()}
          >
            {loading ? <CircularProgress size={24} /> : 'Yorum Yap'}
          </Button>
        </form>
      )}

      <Box sx={{ mt: 2 }}>
        {comments.map((comment) => (
          <Box
            key={comment.id}
            sx={{
              display: 'flex',
              alignItems: 'flex-start',
              mb: 2,
              p: 2,
              bgcolor: 'background.paper',
              borderRadius: 1,
            }}
          >
            <Avatar sx={{ mr: 2 }}>
              {comment.user.username.charAt(0).toUpperCase()}
            </Avatar>
            <Box sx={{ flexGrow: 1 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 0.5 }}>
                <Typography variant="subtitle2" sx={{ fontWeight: 'bold' }}>
                  {comment.user.username}
                </Typography>
                <Typography variant="caption" sx={{ ml: 1, color: 'text.secondary' }}>
                  {formatDate(comment.createdAt)}
                </Typography>
              </Box>
              <Typography variant="body2">{comment.content}</Typography>
            </Box>
            {(currentUser?.id === comment.user.id) && (
              <IconButton
                size="small"
                onClick={() => handleDelete(comment.id)}
                color="error"
              >
                <DeleteIcon />
              </IconButton>
            )}
          </Box>
        ))}
        {comments.length === 0 && (
          <Typography variant="body2" color="text.secondary" sx={{ mt: 2 }}>
            Henüz yorum yapılmamış.
          </Typography>
        )}
      </Box>
    </Box>
  );
};

export default CommentList; 