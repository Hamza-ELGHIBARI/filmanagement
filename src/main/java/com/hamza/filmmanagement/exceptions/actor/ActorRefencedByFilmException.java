package com.hamza.filmmanagement.exceptions.actor;

public class ActorRefencedByFilmException extends RuntimeException {
    public ActorRefencedByFilmException(String message) {
        super(message);
    }
}

