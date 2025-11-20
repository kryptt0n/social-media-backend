package com.example.msosidentity.exceptions;

public class UsernameRequiredException extends RuntimeException{
    public UsernameRequiredException(String message) {
        super(message);
    }
}
