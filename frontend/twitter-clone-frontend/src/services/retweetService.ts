import axios from 'axios';
import authService from './authService';

const API_URL = 'http://localhost:3000/api';

export interface Retweet {
  id: string;
  createdAt: string;
  user: {
    id: string;
    username: string;
  };
  tweet: {
    id: string;
    content: string;
    user: {
      id: string;
      username: string;
    };
  };
}

const retweetService = {
  getAuthHeader() {
    const token = authService.getToken();
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  },

  // Retweet yap
  createRetweet: async (tweetId: string): Promise<Retweet> => {
    const response = await axios.post(
      `${API_URL}/retweets/${tweetId}`,
      {},
      retweetService.getAuthHeader()
    );
    return response.data;
  },

  // Retweet'i geri al
  deleteRetweet: async (tweetId: string): Promise<void> => {
    await axios.delete(
      `${API_URL}/retweets/${tweetId}`,
      retweetService.getAuthHeader()
    );
  },

  // Kullanıcının retweet'lerini getir
  getUserRetweets: async (userId: string): Promise<Retweet[]> => {
    const response = await axios.get(
      `${API_URL}/retweets/user/${userId}`,
      retweetService.getAuthHeader()
    );
    return response.data;
  },

  // Tweet'in retweet edilip edilmediğini kontrol et
  hasUserRetweeted: async (tweetId: string): Promise<boolean> => {
    const response = await axios.get(
      `${API_URL}/retweets/check/${tweetId}`,
      retweetService.getAuthHeader()
    );
    return response.data;
  },
};

export default retweetService; 