package com.example.msssuserprofilecrud.dto;

public record UserProfileDTO(Integer id,
                             String bio,
                             String email,
                             String username,
                             boolean isActive,
                             boolean isPublic) {
}

