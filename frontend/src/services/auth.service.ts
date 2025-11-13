import api from './api';
import { LoginRequest, SignUpRequest, AuthResponse } from '../types';
import { jwtDecode } from 'jwt-decode';

interface JwtPayload {
    sub: string; // user ID
    iat: number;
    exp: number;
}

export const authService = {
    login: async (data: LoginRequest): Promise<AuthResponse> => {
        const response = await api.post<AuthResponse>('/auth/login', data);
        if (response.data.accessToken) {
            const token = response.data.accessToken;
            localStorage.setItem('token', token);
            
            try {
                const decoded = jwtDecode<JwtPayload>(token);
                console.log('Decoded token:', decoded); // Debug için
                if (decoded.sub) {
                    localStorage.setItem('userId', decoded.sub);
                } else {
                    console.error('Token içinde userId bulunamadı');
                }
            } catch (error) {
                console.error('Token decode hatası:', error);
            }
        }
        return response.data;
    },

    register: async (data: SignUpRequest) => {
        const response = await api.post('/auth/register', data);
        return response.data;
    },

    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userId');
    },

    getCurrentUser: () => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decoded = jwtDecode<JwtPayload>(token);
                return decoded.sub ? true : false;
            } catch {
                return false;
            }
        }
        return false;
    },

    getUserId: (): number | null => {
        const userId = localStorage.getItem('userId');
        return userId ? parseInt(userId) : null;
    }
}; 