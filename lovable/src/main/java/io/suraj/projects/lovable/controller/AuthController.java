package io.suraj.projects.lovable.controller;


import io.suraj.projects.lovable.dto.auth.AuthResponse;
import io.suraj.projects.lovable.dto.auth.LoginRequest;
import io.suraj.projects.lovable.dto.auth.SignupRequest;
import io.suraj.projects.lovable.dto.auth.UserProfileRespose;
import io.suraj.projects.lovable.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private  AuthService authService; //later will make final once implements

    public ResponseEntity<AuthResponse> signup(SignupRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    public ResponseEntity<AuthResponse> login(LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    public ResponseEntity<UserProfileRespose> getProfile(){
        Long userId =1L;
        return ResponseEntity.ok(authService.getProfile(userId));
    }

}
