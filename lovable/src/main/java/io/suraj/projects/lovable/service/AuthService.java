package io.suraj.projects.lovable.service;


import io.suraj.projects.lovable.dto.auth.AuthResponse;
import io.suraj.projects.lovable.dto.auth.LoginRequest;
import io.suraj.projects.lovable.dto.auth.SignupRequest;
import io.suraj.projects.lovable.dto.auth.UserProfileRespose;

public interface AuthService {
    
    String signup(SignupRequest request);

    AuthResponse login(LoginRequest request);

    UserProfileRespose getProfile(String userId);
}
