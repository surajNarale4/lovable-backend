package io.suraj.projects.lovable.error;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> badRequestException(BadRequestException ep){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(HttpStatus.BAD_REQUEST,ep.getMessage()));
    }

    @ExceptionHandler(ResourseNotFoundException.class)
    public ResponseEntity<ApiError> ResourceNotFoundException(ResourseNotFoundException ep){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(HttpStatus.NOT_FOUND,ep.getMessage()));
    }
}
