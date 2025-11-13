import React, { useEffect, useState, useCallback, useRef } from 'react';
import {
  Box,
  Paper,
  Typography,
  Avatar,
  Tabs,
  Tab,
  CircularProgress,
  Alert,
} from '@mui/material';
import { useParams } from 'react-router-dom';
import TweetCard from '../tweet/TweetCard';
import tweetService, { Tweet } from '../../services/tweetService';
import authService from '../../services/authService';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;
  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      {...other}
    >
      {value === index && (
        <Box sx={{ py: 3 }}>
          {children}
        </Box>
      )}
    </div>
  );
}

const ProfilePage = () => {
  const { userId } = useParams<{ userId: string }>();
  const [tweets, setTweets] = useState<Tweet[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [tabValue, setTabValue] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [page, setPage] = useState(1);
  const [loadingMore, setLoadingMore] = useState(false);
  const [totalTweets, setTotalTweets] = useState(0);
  const currentUser = authService.getCurrentUser();

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

  const fetchUserTweets = async (pageNumber: number, isInitial: boolean = false) => {
    if (!userId) return;
    
    if (isInitial) {
      setLoading(true);
    } else {
      setLoadingMore(true);
    }
    setError(null);

    try {
      const response = await tweetService.getUserTweets(userId, pageNumber);
      setTweets(prev => isInitial ? response.data : [...prev, ...response.data]);
      setHasMore(response.hasMore);
      setTotalTweets(response.total);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Tweet\'ler yüklenirken bir hata oluştu');
    } finally {
      setLoading(false);
      setLoadingMore(false);
    }
  };

  useEffect(() => {
    fetchUserTweets(1, true);
  }, [userId]);

  useEffect(() => {
    if (page > 1) {
      fetchUserTweets(page);
    }
  }, [page]);

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  const handleTweetDeleted = () => {
    setPage(1);
    fetchUserTweets(1, true);
  };

  const handleTweetLiked = (tweetId: string) => {
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

  const username = tweets[0]?.user.username || 'Kullanıcı';

  return (
    <Box>
      <Paper sx={{ p: 3, mb: 3 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
          <Avatar sx={{ width: 80, height: 80, mr: 2 }}>
            {username.charAt(0).toUpperCase()}
          </Avatar>
          <Box>
            <Typography variant="h5" component="h1">
              {username}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              {totalTweets} Tweet
            </Typography>
          </Box>
        </Box>

        <Tabs value={tabValue} onChange={handleTabChange}>
          <Tab label="Tweet'ler" />
          <Tab label="Beğeniler" disabled />
        </Tabs>
      </Paper>

      <TabPanel value={tabValue} index={0}>
        {tweets.length > 0 ? (
          <>
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
          </>
        ) : (
          <Alert severity="info">
            Bu kullanıcının henüz tweet'i bulunmuyor.
          </Alert>
        )}
      </TabPanel>

      <TabPanel value={tabValue} index={1}>
        <Alert severity="info">
          Bu özellik yakında eklenecek.
        </Alert>
      </TabPanel>
    </Box>
  );
};

export default ProfilePage; 