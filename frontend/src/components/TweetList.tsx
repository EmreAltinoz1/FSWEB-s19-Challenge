import React, { useEffect, useState } from 'react';
import {
  Box,
  Paper,
  TextField,
  Button,
  Typography,
  Stack,
  Divider,
  IconButton,
} from '@mui/material';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import { tweetService } from '../services/tweet.service';
import type { Tweet } from '../types';

interface TweetListProps {
  userId: number;
}

export const TweetList: React.FC<TweetListProps> = ({ userId }) => {
  const [tweets, setTweets] = useState<Tweet[]>([]);
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(false);

  const loadTweets = async () => {
    try {
      setLoading(true);
      const data = await tweetService.getTweetsByUserId(userId);

      // Son atılan en üstte görünsün
      const sorted = [...data].sort((a: any, b: any) => {
        const da = new Date(a.createdAt).getTime();
        const db = new Date(b.createdAt).getTime();
        return db - da;
      });

      setTweets(sorted);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (userId) {
      loadTweets();
    }
  }, [userId]);

  const handleCreateTweet = async () => {
    if (!content.trim()) return;

    try {
      await tweetService.createTweet(content, userId);
      setContent('');
      await loadTweets();
    } catch (e) {
      console.error('Tweet oluşturma hatası:', e);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await tweetService.deleteTweet(id, userId);
      setTweets((prev) => prev.filter((t) => t.id !== id));
    } catch (error) {
      console.error('Tweet silme hatası:', error);
    }
  };

  return (
    <Box
      sx={{
        maxWidth: 620,
        mx: 'auto',
        mt: 4,
        mb: 6,
      }}
    >
      {/* Tweet oluşturma kartı */}
      <Paper
        elevation={6}
        sx={{
          p: 2.5,
          mb: 3,
          borderRadius: 3,
          bgcolor: 'rgba(255,255,255,0.98)',
        }}
      >
        <Typography variant="h6" sx={{ mb: 1, fontWeight: 600 }}>
          Ne düşünüyorsun?
        </Typography>

        <TextField
          multiline
          minRows={3}
          fullWidth
          placeholder="Bugün neler oldu, anlat..."
          value={content}
          onChange={(e) => setContent(e.target.value)}
          sx={{
            '& .MuiOutlinedInput-root': {
              borderRadius: 2,
              bgcolor: '#f9fafb',
            },
          }}
        />

        <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 1.5 }}>
          <Button
            variant="contained"
            onClick={handleCreateTweet}
            disabled={!content.trim()}
            sx={{
              px: 3,
              borderRadius: 999,
              fontWeight: 600,
              background: 'linear-gradient(135deg, #1d9bf0 0%, #0284c7 100%)',
              boxShadow: '0 8px 18px rgba(29,155,240,0.45)',
              '&:hover': {
                background: 'linear-gradient(135deg, #0ea5e9 0%, #0369a1 100%)',
              },
            }}
          >
            Tweet Gönder
          </Button>
        </Box>
      </Paper>

      {/* Timeline başlığı */}
      <Typography
        variant="subtitle2"
        sx={{ color: 'rgba(255,255,255,0.8)', mb: 1 }}
      >
        Son tweetlerin
      </Typography>

      {/* Tweet listesi */}
      <Stack spacing={2}>
        {loading && (
          <Typography sx={{ color: 'rgba(255,255,255,0.8)' }}>
            Yükleniyor...
          </Typography>
        )}

        {!loading && tweets.length === 0 && (
          <Typography sx={{ color: 'rgba(255,255,255,0.8)' }}>
            Henüz hiç tweet atmamışsın. İlk tweetini gönder! 🚀
          </Typography>
        )}

        {tweets.map((tweet) => (
          <Paper
            key={tweet.id}
            elevation={4}
            sx={{
              p: 2,
              borderRadius: 3,
              bgcolor: '#f9fafb',
              border: '1px solid rgba(148,163,184,0.35)',
              transition: 'transform 0.1s ease, box-shadow 0.1s ease',
              position: 'relative',
              '&:hover': {
                transform: 'translateY(-2px)',
                boxShadow: '0 12px 30px rgba(15,23,42,0.35)',
              },
            }}
          >
            {/* Sil ikon butonu */}
            <IconButton
              size="small"
              onClick={() => handleDelete(tweet.id)}
              sx={{
                position: 'absolute',
                top: 8,
                right: 8,
                color: '#94a3b8',
                '&:hover': {
                  color: '#ef4444',
                  backgroundColor: 'rgba(248,113,113,0.08)',
                },
              }}
            >
              <DeleteOutlineIcon fontSize="small" />
            </IconButton>

            <Typography
              variant="body1"
              sx={{ mb: 1, wordBreak: 'break-word' }}
            >
              {tweet.content}
            </Typography>

            <Divider sx={{ my: 1 }} />

            <Typography
              variant="caption"
              sx={{ color: '#64748b', fontSize: 11 }}
            >
              {tweet.createdAt
                ? new Date(tweet.createdAt).toLocaleString('tr-TR')
                : ''}
            </Typography>
          </Paper>
        ))}
      </Stack>

      <Box sx={{ mt: 3 }}>
        <Typography
          variant="caption"
          sx={{ color: 'rgba(255,255,255,0.7)' }}
        >
          Kullanıcı ID: {userId}
        </Typography>
      </Box>
    </Box>
  );
};
