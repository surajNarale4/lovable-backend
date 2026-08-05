package io.suraj.projects.lovable.service;


import io.suraj.projects.lovable.doto.auth.AuthResponse;
import io.suraj.projects.lovable.doto.auth.LoginRequest;
import io.suraj.projects.lovable.doto.auth.SignupRequest;
import io.suraj.projects.lovable.doto.auth.UserProfileRespose;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);

    UserProfileRespose getProfile(Long userId);
}
