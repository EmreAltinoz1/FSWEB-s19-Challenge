package com.twitterclone.twitterapi.service;

import com.twitterclone.twitterapi.dto.JwtAuthenticationResponse;
import com.twitterclone.twitterapi.dto.LoginRequest;
import com.twitterclone.twitterapi.dto.SignUpRequest;
import com.twitterclone.twitterapi.entity.User;

public interface AuthService {
    JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);
    User registerUser(SignUpRequest signUpRequest);
} 