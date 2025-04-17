package com.example.msssuserprofilecrud.dto;

public record UserProfileDTO(Integer id,
                             String bio,
                             String email,
                             boolean isActive,
                             boolean isPublic) {
}

