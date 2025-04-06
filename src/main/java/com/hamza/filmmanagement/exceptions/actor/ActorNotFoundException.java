package com.hamza.filmmanagement.exceptions.actor;

public class ActorNotFoundException extends RuntimeException {
    public ActorNotFoundException(String message) {
        super(message);
    }
}
