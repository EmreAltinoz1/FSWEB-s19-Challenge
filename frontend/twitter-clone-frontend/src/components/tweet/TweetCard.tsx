import React, { useState } from 'react';
import {
  Card,
  CardContent,
  Typography,
  CardActions,
  IconButton,
  Box,
  Avatar,
} from '@mui/material';
import {
  Favorite as FavoriteIcon,
  FavoriteBorder as FavoriteBorderIcon,
  Delete as DeleteIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { Tweet } from '../../services/tweetService';
import tweetService from '../../services/tweetService';
import authService from '../../services/authService';

interface TweetCardProps {
  tweet: Tweet;
  onTweetDeleted: () => void;
  onTweetLiked: (tweetId: string) => void;
}

const TweetCard: React.FC<TweetCardProps> = ({
  tweet,
  onTweetDeleted,
  onTweetLiked,
}) => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const currentUser = authService.getCurrentUser();
  const isOwner = currentUser?.id === tweet.user.id;

  const handleLike = async () => {
    if (loading) return;
    setLoading(true);
    try {
      await tweetService.toggleLike(tweet.id);
      onTweetLiked(tweet.id);
    } catch (error) {
      console.error('Tweet beğenilirken hata oluştu:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (loading || !isOwner) return;
    setLoading(true);
    try {
      await tweetService.deleteTweet(tweet.id);
      onTweetDeleted();
    } catch (error) {
      console.error('Tweet silinirken hata oluştu:', error);
    } finally {
      setLoading(false);
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

  const handleUserClick = () => {
    navigate(`/profile/${tweet.user.id}`);
  };

  return (
    <Card sx={{ mb: 2 }}>
      <CardContent>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
          <Avatar 
            sx={{ mr: 1, cursor: 'pointer' }}
            onClick={handleUserClick}
          >
            {tweet.user.username.charAt(0).toUpperCase()}
          </Avatar>
          <Box>
            <Typography 
              variant="subtitle1" 
              component="span" 
              sx={{ 
                fontWeight: 'bold',
                cursor: 'pointer',
                '&:hover': {
                  textDecoration: 'underline',
                }
              }}
              onClick={handleUserClick}
            >
              {tweet.user.username}
            </Typography>
            <Typography variant="caption" color="text.secondary" sx={{ ml: 1 }}>
              {formatDate(tweet.createdAt)}
            </Typography>
          </Box>
        </Box>
        <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap' }}>
          {tweet.content}
        </Typography>
      </CardContent>
      <CardActions disableSpacing>
        <IconButton
          onClick={handleLike}
          disabled={loading}
          color={tweet.isLiked ? 'primary' : 'default'}
        >
          {tweet.isLiked ? <FavoriteIcon /> : <FavoriteBorderIcon />}
        </IconButton>
        <Typography variant="caption" color="text.secondary">
          {tweet.likes}
        </Typography>
        {isOwner && (
          <IconButton
            onClick={handleDelete}
            disabled={loading}
            sx={{ ml: 'auto' }}
            color="error"
          >
            <DeleteIcon />
          </IconButton>
        )}
      </CardActions>
    </Card>
  );
};

export default TweetCard; 