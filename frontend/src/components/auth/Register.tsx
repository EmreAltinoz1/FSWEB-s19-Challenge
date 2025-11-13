import React, { useState } from 'react';
import {
  Box,
  Button,
  TextField,
  Typography,
  Alert,
  Paper,
  Avatar,
  List,
  ListItem,
  ListItemText,
} from '@mui/material';
import PersonAddAlt1Icon from '@mui/icons-material/PersonAddAlt1';
import { authService } from '../../services/auth.service';
import { useNavigate, Link } from 'react-router-dom';

export const Register: React.FC = () => {
  const [username, setUsername]   = useState('');
  const [email, setEmail]         = useState('');
  const [password, setPassword]   = useState('');
  const [errors, setErrors]       = useState<string[]>([]);
  const navigate = useNavigate();

    const passwordRequirements = [
      {
        test: (v: string) => v.length >= 6,
        message: 'Şifre en az 6 karakter olmalı.',
      },
    ];


  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const collectedErrors: string[] = [];

    // Önce FE tarafı şifre validasyonu
    passwordRequirements.forEach(req => {
      if (!req.test(password)) collectedErrors.push(req.message);
    });

    if (collectedErrors.length > 0) {
      setErrors(collectedErrors);
      return;
    }

    try {
      await authService.register({ username, email, password });
      navigate('/login');
    } catch (err: any) {
      console.error('Register error:', err);

      const status = err?.response?.status;
      const data   = err?.response?.data;

      let backendErrors: string[] = [];

      if (status === 400 && data) {
        const rawText =
          (typeof data === 'string'
            ? data
            : data.message || data.error || JSON.stringify(data)
          ).toString()
          .toLowerCase();

        const hasUsername = rawText.includes('username') || rawText.includes('kullanıcı');
        const hasEmail    = rawText.includes('email') || rawText.includes('e-posta') || rawText.includes('eposta') || rawText.includes('mail');
        const usedWords   = ['exist', 'already', 'in use', 'used', 'zaten', 'kayıtlı', 'kullanımda', 'mevcut'];

        const emailAlready =
          hasEmail && usedWords.some((w) => rawText.includes(w));

        const usernameAlready =
          hasUsername && usedWords.some((w) => rawText.includes(w));

        const emailInvalid =
          hasEmail &&
          (rawText.includes('valid') ||
           rawText.includes('format') ||
           rawText.includes('geçerli'));

        const passwordInvalid =
          rawText.includes('password') || rawText.includes('şifre');

        if (usernameAlready) backendErrors.push('Bu kullanıcı adı zaten kullanımda.');
        if (emailAlready) backendErrors.push('Bu e-posta adresiyle zaten bir hesap var.');
        if (emailInvalid) backendErrors.push('Lütfen geçerli bir e-posta adresi girin.');
        if (passwordInvalid) backendErrors.push('Şifre gereksinimlerini karşılamıyor.');

        // Eğer tanımlı bir hata yakalayamazsak generic ekle
        if (backendErrors.length === 0) {
          backendErrors.push('Kayıt başarısız. Lütfen bilgilerinizi kontrol edin.');
        }
      } else {
        backendErrors.push('Sunucuya bağlanırken bir hata oluştu.');
      }

      setErrors(backendErrors);
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
        {/* Header */}
        <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mb: 2 }}>
          <Avatar
            sx={{
              mb: 2,
              bgcolor: '#22c55e',
              width: 56,
              height: 56,
              boxShadow: '0 12px 30px rgba(34,197,94,0.55)',
            }}
          >
            <PersonAddAlt1Icon sx={{ fontSize: 30 }} />
          </Avatar>

          <Typography component="h1" variant="h5" sx={{ fontWeight: 700 }}>
            Kayıt Ol
          </Typography>
          <Typography variant="body2" sx={{ color: '#64748b', mt: 0.5 }}>
            Yeni bir hesap oluşturun
          </Typography>
        </Box>

        {/* Çoklu hata gösterimi */}
        {errors.length > 0 && (
          <Alert severity="error" sx={{ mb: 2 }}>
            <List dense>
              {errors.map((err, i) => (
                <ListItem key={i} sx={{ py: 0 }}>
                  <ListItemText primary={err} />
                </ListItem>
              ))}
            </List>
          </Alert>
        )}

        {/* Form */}
        <Box component="form" onSubmit={handleSubmit}>
          <TextField
            margin="normal"
            required
            fullWidth
            label="Kullanıcı Adı"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            sx={{ '& .MuiOutlinedInput-root': { borderRadius: 2, bgcolor: '#fff' } }}
          />

          <TextField
            margin="normal"
            required
            fullWidth
            label="E-posta Adresi"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            sx={{ '& .MuiOutlinedInput-root': { borderRadius: 2, bgcolor: '#fff' } }}
          />

          <TextField
            margin="normal"
            required
            fullWidth
            label="Şifre"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            sx={{ '& .MuiOutlinedInput-root': { borderRadius: 2, bgcolor: '#fff' } }}
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
              background: 'linear-gradient(135deg, #22c55e 0%, #16a34a 100%)',
              boxShadow: '0 12px 28px rgba(34,197,94,0.55)',
              '&:hover': {
                background: 'linear-gradient(135deg, #4ade80 0%, #16a34a 100%)',
                boxShadow: '0 16px 34px rgba(34,197,94,0.75)',
              },
            }}
          >
            Kayıt Ol
          </Button>
        </Box>

        <Typography variant="body2" sx={{ textAlign: 'center', mt: 2, color: '#64748b' }}>
          Zaten hesabınız var mı?{' '}
          <Link to="/login" style={{ color: '#1d9bf0', fontWeight: 600, textDecoration: 'none' }}>
            Giriş yapın
          </Link>
        </Typography>
      </Paper>
    </Box>
  );
};
