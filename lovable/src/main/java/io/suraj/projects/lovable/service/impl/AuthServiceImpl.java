package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.auth.AuthResponse;
import io.suraj.projects.lovable.dto.auth.LoginRequest;
import io.suraj.projects.lovable.dto.auth.SignupRequest;
import io.suraj.projects.lovable.dto.auth.UserProfileRespose;
import io.suraj.projects.lovable.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse signup(SignupRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }

    @Override
    public UserProfileRespose getProfile(Long userId) {
        return null;
    }
}
