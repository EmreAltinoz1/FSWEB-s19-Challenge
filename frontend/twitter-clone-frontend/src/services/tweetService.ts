import axios from 'axios';
import authService from './authService';

const API_URL = 'http://localhost:3000/api';

export interface Tweet {
  id: string;
  content: string;
  createdAt: string;
  likes: number;
  isLiked: boolean;
  user: {
    id: string;
    username: string;
  };
}

export interface CreateTweetData {
  content: string;
}

export interface PaginatedResponse<T> {
  data: T[];
  hasMore: boolean;
  nextPage: number;
  total: number;
}

const tweetService = {
  // Axios instance with auth header
  getAuthHeader() {
    const token = authService.getToken();
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  },

  // Tweet'leri getir
  getAllTweets: async (page: number = 1, limit: number = 10): Promise<PaginatedResponse<Tweet>> => {
    const response = await axios.get(
      `${API_URL}/tweets?page=${page}&limit=${limit}`,
      tweetService.getAuthHeader()
    );
    return response.data;
  },

  // Yeni tweet oluştur
  createTweet: async (data: CreateTweetData): Promise<Tweet> => {
    const response = await axios.post(
      `${API_URL}/tweets`,
      data,
      tweetService.getAuthHeader()
    );
    return response.data;
  },

  // Tweet sil
  deleteTweet: async (tweetId: string): Promise<void> => {
    await axios.delete(
      `${API_URL}/tweets/${tweetId}`,
      tweetService.getAuthHeader()
    );
  },

  // Tweet beğen/beğenmekten vazgeç
  toggleLike: async (tweetId: string): Promise<Tweet> => {
    const response = await axios.post(
      `${API_URL}/tweets/${tweetId}/like`,
      {},
      tweetService.getAuthHeader()
    );
    return response.data;
  },

  // Kullanıcının kendi tweet'lerini getir
  getUserTweets: async (userId: string, page: number = 1, limit: number = 10): Promise<PaginatedResponse<Tweet>> => {
    const response = await axios.get(
      `${API_URL}/tweets/user/${userId}?page=${page}&limit=${limit}`,
      tweetService.getAuthHeader()
    );
    return response.data;
  },
};

export default tweetService; 