package com.example.msosidentity.dto;

public record UserShortDTO(boolean exists, String email, String username, Integer userId) { }
