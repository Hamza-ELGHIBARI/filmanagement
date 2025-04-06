package com.hamza.filmmanagement.exceptions.director;

public class DirectorNotFoundException extends RuntimeException {
    public DirectorNotFoundException(String message) {
        super(message);
    }
}