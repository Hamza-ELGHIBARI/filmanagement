package com.hamza.filmmanagement.exceptions.auth;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}