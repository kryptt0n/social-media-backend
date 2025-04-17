package com.example.msosuserprofile.dto;

import lombok.Data;

@Data
public class UserDataResponseDto {
    private String username;
    private String imageUrl;
    private String bio;
    boolean isActive;
    boolean isPublic;
}
