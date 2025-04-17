package com.example.msosuserprofile.dto;

public record UserProfileDTO(Integer id,
                             String bio,
                             String email,
                             boolean isActive,
                             boolean isPublic) {
}

