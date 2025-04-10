package com.socialmedia.mssspost.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostRequestDto {
    @NotBlank(message = "Content must not be empty")
    private String content;
    private String imageUrl;

    @NotNull(message = "User ID is required")
    private Integer userId;
}

