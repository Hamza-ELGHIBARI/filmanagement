package com.hamza.filmmanagement.exceptions;

import com.hamza.filmmanagement.dto.ApiResponse;
import com.hamza.filmmanagement.exceptions.actor.ActorNotFoundException;
import com.hamza.filmmanagement.exceptions.actor.ActorRefencedByFilmException;
import com.hamza.filmmanagement.exceptions.auth.*;
import com.hamza.filmmanagement.exceptions.director.DirectorNotFoundException;
import com.hamza.filmmanagement.exceptions.email.SendingEmailException;
import com.hamza.filmmanagement.exceptions.film.FilmNotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ApiResponse response = new ApiResponse("User already exists", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse> handleInvalidTokenException(InvalidTokenException ex) {
        ApiResponse response = new ApiResponse("Invalid Token", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ApiResponse response = new ApiResponse("User already exists", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SendingEmailException.class)
    public ResponseEntity<ApiResponse> handleSendingEmailException(InvalidTokenException ex) {
        ApiResponse response = new ApiResponse("Invalid Token", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEmailNotFoundException(EmailNotFoundException ex) {
        ApiResponse response = new ApiResponse("Error", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ApiResponse> handleMessagingException(MessagingException ex) {
        ApiResponse response = new ApiResponse("Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ActorNotFoundException.class)
    public ResponseEntity<ApiResponse> handleActorNotFoundException(ActorNotFoundException ex) {
        ApiResponse response = new ApiResponse("Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DirectorNotFoundException.class)
    public ResponseEntity<ApiResponse> handleDirectorNotFoundException(DirectorNotFoundException ex) {
        ApiResponse response = new ApiResponse("Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FilmNotFoundException.class)
    public ResponseEntity<ApiResponse> handleFilmNotFoundException(FilmNotFoundException ex) {
        ApiResponse response = new ApiResponse("Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ActorRefencedByFilmException.class)
    public ResponseEntity<ApiResponse> handleActorRefencedByFilmException(ActorRefencedByFilmException ex) {
        ApiResponse response = new ApiResponse("Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountIsNotEnabledException.class)
    public ResponseEntity<ApiResponse> handleAccountIsNotEnabledException(AccountIsNotEnabledException ex) {
        ApiResponse response = new ApiResponse("Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        ApiResponse response = new ApiResponse("Internal Error", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception e) {
        ApiResponse response = new ApiResponse("Error", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
