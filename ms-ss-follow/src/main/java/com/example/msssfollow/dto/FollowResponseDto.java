package com.example.msssfollow.dto;

import lombok.Data;

@Data
public class FollowResponseDto {
    private boolean isFollowed;
    private Integer followerCount;
    private Integer followedCount;
}
