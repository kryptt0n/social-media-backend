package com.example.msosidentity.exceptions;

public class CacheNotFoundException extends RuntimeException{
    public CacheNotFoundException(String message) {
        super(message);
    }
}
