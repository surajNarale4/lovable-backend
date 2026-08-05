package io.suraj.projects.lovable.controller;


import io.suraj.projects.lovable.doto.auth.AuthResponse;
import io.suraj.projects.lovable.doto.auth.LoginRequest;
import io.suraj.projects.lovable.doto.auth.SignupRequest;
import io.suraj.projects.lovable.doto.auth.UserProfileRespose;
import io.suraj.projects.lovable.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private AuthService authService;

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
