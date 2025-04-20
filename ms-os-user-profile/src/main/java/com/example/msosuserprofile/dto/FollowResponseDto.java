package com.example.msosuserprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowResponseDto {
//    private boolean isFollowed;
    private Integer followerCount;
    private Integer followedCount;
}
