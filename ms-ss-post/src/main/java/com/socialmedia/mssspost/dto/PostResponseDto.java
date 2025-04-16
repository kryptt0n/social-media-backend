package com.socialmedia.mssspost.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponseDto {
    private Integer id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
}