package com.hamza.filmmanagement.exceptions.auth;

public class AccountIsNotEnabledException extends RuntimeException {
    public AccountIsNotEnabledException(String message) {
        super(message);
    }
}
