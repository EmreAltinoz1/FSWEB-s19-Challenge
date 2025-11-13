import axios from 'axios';
import authService from './authService';

const API_URL = 'http://localhost:3000/api';

export interface Comment {
  id: string;
  content: string;
  createdAt: string;
  user: {
    id: string;
    username: string;
  };
}

const commentService = {
  getAuthHeader() {
    const token = authService.getToken();
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  },

  // Yorumları getir
  getCommentsByTweetId: async (tweetId: string): Promise<Comment[]> => {
    const response = await axios.get(
      `${API_URL}/comments/tweet/${tweetId}`,
      commentService.getAuthHeader()
    );
    return response.data;
  },

  // Yorum oluştur
  createComment: async (tweetId: string, content: string): Promise<Comment> => {
    const response = await axios.post(
      `${API_URL}/comments`,
      { tweetId, content },
      commentService.getAuthHeader()
    );
    return response.data;
  },

  // Yorum güncelle
  updateComment: async (commentId: string, content: string): Promise<Comment> => {
    const response = await axios.put(
      `${API_URL}/comments/${commentId}`,
      { content },
      commentService.getAuthHeader()
    );
    return response.data;
  },

  // Yorum sil
  deleteComment: async (commentId: string): Promise<void> => {
    await axios.delete(
      `${API_URL}/comments/${commentId}`,
      commentService.getAuthHeader()
    );
  },
};

export default commentService; 