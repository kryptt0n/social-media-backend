package com.example.mssscredentials.dto;

public record UserProfileDTO(Integer id,
                             String bio,
                             String email,
                             boolean isActive,
                             boolean isPublic) {
}

