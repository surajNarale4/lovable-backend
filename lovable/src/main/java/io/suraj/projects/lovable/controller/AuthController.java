package io.suraj.projects.lovable.controller;


import io.suraj.projects.lovable.dto.auth.AuthResponse;
import io.suraj.projects.lovable.dto.auth.LoginRequest;
import io.suraj.projects.lovable.dto.auth.SignupRequest;
import io.suraj.projects.lovable.dto.auth.UserProfileRespose;
import io.suraj.projects.lovable.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService; //later will make final once implements
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(SignupRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileRespose> getProfile(){
        Long userId =1L;
        return ResponseEntity.ok(authService.getProfile(userId));
    }

}
