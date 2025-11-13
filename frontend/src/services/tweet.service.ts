import api from './api';
import { Tweet } from '../types';

export const tweetService = {
    createTweet: async (content: string, userId: number): Promise<Tweet> => {
        try {
            const response = await api.post<Tweet>(`/api/tweets/user/${userId}`, { content });
            return response.data;
        } catch (error) {
            console.error('Tweet oluşturma hatası:', error);
            throw error;
        }
    },

    getTweetsByUserId: async (userId: number): Promise<Tweet[]> => {
        try {
            const response = await api.get<Tweet[]>(`/api/tweets/user/${userId}`);
            // API yanıtının bir dizi olduğundan emin oluyoruz
            if (!Array.isArray(response.data)) {
                console.error('API yanıtı bir dizi değil:', response.data);
                return [];
            }
            return response.data;
        } catch (error) {
            console.error('Tweet\'leri getirme hatası:', error);
            return [];
        }
    },

    getTweetById: async (id: number): Promise<Tweet> => {
        try {
            const response = await api.get<Tweet>(`/api/tweets/${id}`);
            return response.data;
        } catch (error) {
            console.error('Tweet detayı getirme hatası:', error);
            throw error;
        }
    },

    updateTweet: async (id: number, content: string): Promise<Tweet> => {
        try {
            const response = await api.put<Tweet>(`/api/tweets/${id}`, { content });
            return response.data;
        } catch (error) {
            console.error('Tweet güncelleme hatası:', error);
            throw error;
        }
    },

    deleteTweet: async (id: number, userId: number): Promise<void> => {
        try {
            await api.delete(`/api/tweets/${id}?userId=${userId}`);
        } catch (error) {
            console.error('Tweet silme hatası:', error);
            throw error;
        }
    }
};