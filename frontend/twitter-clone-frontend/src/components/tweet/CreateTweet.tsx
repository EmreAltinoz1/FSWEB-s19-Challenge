import React, { useState } from 'react';
import {
  Paper,
  TextField,
  Button,
  Box,
  CircularProgress,
  Alert,
} from '@mui/material';
import tweetService from '../../services/tweetService';

interface CreateTweetProps {
  onTweetCreated: () => void;
}

const CreateTweet: React.FC<CreateTweetProps> = ({ onTweetCreated }) => {
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const maxLength = 280;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!content.trim()) return;

    setLoading(true);
    setError(null);

    try {
      await tweetService.createTweet({ content: content.trim() });
      setContent('');
      onTweetCreated();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Tweet oluşturulurken bir hata oluştu');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Paper sx={{ p: 2, mb: 3 }}>
      <form onSubmit={handleSubmit}>
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}
        <TextField
          fullWidth
          multiline
          rows={3}
          placeholder="Ne düşünüyorsun?"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          disabled={loading}
          error={content.length > maxLength}
          helperText={`${content.length}/${maxLength}`}
        />
        <Box sx={{ mt: 2, display: 'flex', justifyContent: 'flex-end' }}>
          <Button
            variant="contained"
            type="submit"
            disabled={loading || !content.trim() || content.length > maxLength}
          >
            {loading ? <CircularProgress size={24} /> : 'Tweet'}
          </Button>
        </Box>
      </form>
    </Paper>
  );
};

export default CreateTweet; 