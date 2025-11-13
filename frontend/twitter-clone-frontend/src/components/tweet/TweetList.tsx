import React, { useEffect, useState, useCallback, useRef } from 'react';
import { Box, CircularProgress, Alert } from '@mui/material';
import TweetCard from './TweetCard';
import CreateTweet from './CreateTweet';
import tweetService, { Tweet } from '../../services/tweetService';
import authService from '../../services/authService';

const TweetList = () => {
  const [tweets, setTweets] = useState<Tweet[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [hasMore, setHasMore] = useState(true);
  const [page, setPage] = useState(1);
  const [loadingMore, setLoadingMore] = useState(false);
  const isAuthenticated = authService.isAuthenticated();
  const observer = useRef<IntersectionObserver | null>(null);
  const lastTweetElementRef = useCallback((node: HTMLDivElement) => {
    if (loading || loadingMore) return;
    
    if (observer.current) observer.current.disconnect();
    
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
        setPage(prevPage => prevPage + 1);
      }
    });
    
    if (node) observer.current.observe(node);
  }, [loading, loadingMore, hasMore]);

  const fetchTweets = async (pageNumber: number, isInitial: boolean = false) => {
    if (isInitial) {
      setLoading(true);
    } else {
      setLoadingMore(true);
    }
    setError(null);

    try {
      const response = await tweetService.getAllTweets(pageNumber);
      setTweets(prev => isInitial ? response.data : [...prev, ...response.data]);
      setHasMore(response.hasMore);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Tweet\'ler yüklenirken bir hata oluştu');
    } finally {
      setLoading(false);
      setLoadingMore(false);
    }
  };

  useEffect(() => {
    fetchTweets(1, true);
  }, []);

  useEffect(() => {
    if (page > 1) {
      fetchTweets(page);
    }
  }, [page]);

  const handleTweetCreated = () => {
    setPage(1);
    fetchTweets(1, true);
  };

  const handleTweetDeleted = () => {
    setPage(1);
    fetchTweets(1, true);
  };

  const handleTweetLiked = async (tweetId: string) => {
    setTweets(tweets.map(tweet => {
      if (tweet.id === tweetId) {
        return {
          ...tweet,
          isLiked: !tweet.isLiked,
          likes: tweet.isLiked ? tweet.likes - 1 : tweet.likes + 1,
        };
      }
      return tweet;
    }));
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mt: 2 }}>
        {error}
      </Alert>
    );
  }

  return (
    <Box>
      {isAuthenticated && <CreateTweet onTweetCreated={handleTweetCreated} />}
      {tweets.map((tweet, index) => (
        <div
          key={tweet.id}
          ref={index === tweets.length - 1 ? lastTweetElementRef : undefined}
        >
          <TweetCard
            tweet={tweet}
            onTweetDeleted={handleTweetDeleted}
            onTweetLiked={handleTweetLiked}
          />
        </div>
      ))}
      {loadingMore && (
        <Box sx={{ display: 'flex', justifyContent: 'center', my: 2 }}>
          <CircularProgress />
        </Box>
      )}
      {tweets.length === 0 && (
        <Alert severity="info">Henüz hiç tweet yok.</Alert>
      )}
    </Box>
  );
};

export default TweetList; 