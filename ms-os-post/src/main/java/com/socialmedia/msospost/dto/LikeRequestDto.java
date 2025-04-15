package com.socialmedia.msospost.dto;

import lombok.Data;

@Data
public class LikeRequestDto {
    private String username;
    private Integer postId;
}
