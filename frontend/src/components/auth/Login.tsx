import React, { useState } from 'react';
import {
  Box,
  Button,
  TextField,
  Typography,
  Alert,
  Paper,
  Avatar,
  Divider,
} from '@mui/material';
import { authService } from '../../services/auth.service';
import { useNavigate, Link } from 'react-router-dom';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';

export const Login: React.FC = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await authService.login({ username, password });
      navigate('/');
    } catch (err) {
      setError('Giriş başarısız. Lütfen kullanıcı adı ve şifrenizi kontrol edin.');
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        px: 2,
      }}
    >
      <Paper
        elevation={0}
        sx={{
          width: 420,
          maxWidth: '100%',
          px: 4,
          py: 5,
          borderRadius: 4,
          bgcolor: '#f9fafb',
          boxShadow: '0 32px 80px rgba(15,23,42,0.70)',
          border: '1px solid rgba(148,163,184,0.45)',
        }}
      >
        <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mb: 2 }}>
          <Avatar
            sx={{
              mb: 2,
              bgcolor: '#1d9bf0',
              width: 56,
              height: 56,
              boxShadow: '0 12px 30px rgba(29,155,240,0.55)',
            }}
          >
            <LockOutlinedIcon sx={{ fontSize: 30 }} />
          </Avatar>

          <Typography component="h1" variant="h5" sx={{ fontWeight: 700 }}>
            Twitter Clone
          </Typography>
          <Typography variant="body2" sx={{ color: '#64748b', mt: 0.5 }}>
            Hesabınıza giriş yapın
          </Typography>
        </Box>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit}>
          <TextField
            margin="normal"
            required
            fullWidth
            id="username"
            label="Kullanıcı Adı"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            sx={{
              '& .MuiOutlinedInput-root': {
                borderRadius: 2,
                bgcolor: '#ffffff',
              },
            }}
          />

          <TextField
            margin="normal"
            required
            fullWidth
            label="Şifre"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            sx={{
              '& .MuiOutlinedInput-root': {
                borderRadius: 2,
                bgcolor: '#ffffff',
              },
            }}
          />

          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{
              mt: 3,
              mb: 1,
              py: 1.2,
              fontWeight: 600,
              borderRadius: 999,
              background: 'linear-gradient(135deg, #1d9bf0 0%, #0284c7 100%)',
              boxShadow: '0 12px 28px rgba(29,155,240,0.55)',
              '&:hover': {
                background: 'linear-gradient(135deg, #0ea5e9 0%, #0369a1 100%)',
                boxShadow: '0 16px 34px rgba(29,155,240,0.75)',
              },
            }}
          >
            GİRİŞ YAP
          </Button>
        </Box>

        <Divider sx={{ my: 2 }} />

        <Typography variant="body2" sx={{ textAlign: 'center', color: '#64748b' }}>
          Hesabınız yok mu?{' '}
          <Link to="/register" style={{ color: '#1d9bf0', fontWeight: 600, textDecoration: 'none' }}>
            Kayıt olun
          </Link>
        </Typography>
      </Paper>
    </Box>
  );
};
