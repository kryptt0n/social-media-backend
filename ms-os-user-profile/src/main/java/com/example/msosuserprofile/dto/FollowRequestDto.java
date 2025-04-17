package com.example.msosuserprofile.dto;

import lombok.Data;

@Data
public class FollowRequestDto {
    private Integer followerId;
    private Integer followedId;
}
