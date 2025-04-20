package com.example.msosadmin.dto;

public record UserProfileDTO(Integer id,
                             String username,
                             String bio,
                             String email,
                             boolean isActive,
                             boolean isPublic) {
}

