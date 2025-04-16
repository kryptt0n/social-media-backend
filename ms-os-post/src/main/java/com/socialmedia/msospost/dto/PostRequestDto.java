package com.socialmedia.msospost.dto;

import lombok.Data;

@Data
public class PostRequestDto {
    private Integer userId;
    private String username;
    private String content;
}
