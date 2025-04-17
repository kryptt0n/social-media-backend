package com.example.msosuserprofile.dto;

import lombok.Data;

@Data
public class UserDataResponseDto {
    private String username;
    private String imageUrl;
    private String bio;
    private Integer followerCount;
    private Integer followingCount;
    boolean isActive;
    boolean isPublic;
}
