export interface Tweet {
    id: number;
    content: string;
    userId: number;
    createdAt: string;
    updatedAt: string;
}

export interface User {
    id: number;
    username: string;
    email: string;
    displayName?: string;
    bio?: string;
}

export interface Comment {
    id: number;
    content: string;
    userId: number;
    tweetId: number;
    createdAt: string;
}

export interface Like {
    id: number;
    userId: number;
    tweetId: number;
}

export interface Retweet {
    id: number;
    userId: number;
    tweetId: number;
    createdAt: string;
}

export interface LoginRequest {
    username: string;
    password: string;
}

export interface SignUpRequest {
    username: string;
    email: string;
    password: string;
    displayName?: string;
    bio?: string;
}

export interface AuthResponse {
    accessToken: string;
    tokenType: string;
} 