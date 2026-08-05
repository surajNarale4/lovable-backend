package io.suraj.projects.lovable.doto.auth;

public record SignupRequest(
        String email,
        String name,
        String password
){
}
