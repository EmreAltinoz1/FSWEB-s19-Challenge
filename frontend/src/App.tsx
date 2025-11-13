import React, { useEffect, useState } from 'react';
import { Container, Typography, Button, Box } from '@mui/material';
import { TweetList } from './components/TweetList';
import { Login } from './components/auth/Login';
import { Register } from './components/auth/Register';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import { authService } from './services/auth.service';

function AppContent() {
  const isAuthenticated = localStorage.getItem('token') !== null;
  const [userId, setUserId] = useState<number | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      const currentUserId = authService.getUserId();
      setUserId(currentUserId);
    }
  }, [isAuthenticated]);

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <Container maxWidth="md">
      {/* Üst bar */}
      <Box sx={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', mt: 4, mb: 4 }}>
        {isAuthenticated && (
          <Button
            onClick={handleLogout}
            variant="outlined"
            sx={{
              textTransform: 'none',
              fontWeight: 500,
              fontSize: 14,
              px: 2.5,
              py: 0.5,
              borderRadius: 999,
              color: 'rgba(255,255,255,0.9)',
              border: '1px solid rgba(255,255,255,0.6)',
              backgroundColor: 'rgba(15,23,42,0.25)',
              backdropFilter: 'blur(6px)',
              '&:hover': {
                backgroundColor: 'rgba(15,23,42,0.45)',
                borderColor: 'rgba(255,255,255,0.9)',
              },
            }}
          >
            Çıkış yap
          </Button>
        )}
      </Box>

      <Routes>
        <Route
          path="/login"
          element={isAuthenticated ? <Navigate to="/" /> : <Login />}
        />
        <Route
          path="/register"
          element={isAuthenticated ? <Navigate to="/" /> : <Register />}
        />
        <Route
          path="/"
          element={
            isAuthenticated ? (
              userId ? (
                <TweetList userId={userId} />
              ) : (
                <Typography sx={{ color: 'white' }}>
                  Kullanıcı ID yüklenemedi. Lütfen tekrar giriş yapın.
                </Typography>
              )
            ) : (
              <Navigate to="/login" />
            )
          }
        />
      </Routes>
    </Container>
  );
}

function App() {
  return (
    <Router>
      <AppContent />
    </Router>
  );
}

export default App;
