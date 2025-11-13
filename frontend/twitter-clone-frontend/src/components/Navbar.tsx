import React, { useEffect, useState } from 'react';
import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';

const Navbar = () => {
    const navigate = useNavigate();
    const [isLoggedIn, setIsLoggedIn] = useState(authService.isAuthenticated());
    const [user, setUser] = useState(authService.getCurrentUser());

    useEffect(() => {
        const currentUser = authService.getCurrentUser();
        if (currentUser) {
            setUser(currentUser);
        }
    }, []);

    const handleLogout = () => {
        authService.logout();
        setIsLoggedIn(false);
        setUser(null);
        navigate('/auth');
    };

    const handleUserClick = () => {
        if (user) {
            navigate(`/profile/${user.id}`);
        }
    };

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography 
                    variant="h6" 
                    component="div" 
                    sx={{ flexGrow: 1, cursor: 'pointer' }}
                    onClick={() => navigate('/')}
                >
                    Twitter Clone
                </Typography>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    {isLoggedIn && user && (
                        <Typography 
                            variant="body1"
                            sx={{ 
                                cursor: 'pointer',
                                '&:hover': {
                                    textDecoration: 'underline'
                                }
                            }}
                            onClick={handleUserClick}
                        >
                            {user.username}
                        </Typography>
                    )}
                    {isLoggedIn ? (
                        <Button color="inherit" onClick={handleLogout}>
                            Çıkış Yap
                        </Button>
                    ) : (
                        <Button color="inherit" onClick={() => navigate('/auth')}>
                            Giriş Yap
                        </Button>
                    )}
                </Box>
            </Toolbar>
        </AppBar>
    );
};

export default Navbar; 