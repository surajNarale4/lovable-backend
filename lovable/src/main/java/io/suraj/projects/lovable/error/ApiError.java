package io.suraj.projects.lovable.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.Instant;


public record ApiError(HttpStatus status, String message, Instant timestamp) {
    public ApiError(HttpStatus status,String message){
        this(status,message,Instant.now());
    }
}
