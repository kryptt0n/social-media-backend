package com.socialmedia.msospost.dto;

import lombok.Data;

@Data
public class CreatePostRequestDto {
    private String username;
    private String content;
    private String imageUrl;
}
