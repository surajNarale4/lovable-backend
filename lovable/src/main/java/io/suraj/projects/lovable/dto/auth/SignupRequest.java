package io.suraj.projects.lovable.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.ToString;


public record SignupRequest(
        @Email @NotNull String email,
        @NotNull  String name,
        @Size(min= 4) String password
){
}
