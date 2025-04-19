package com.socialmedia.mssspost.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePostRequestDto {
    @NotNull(message = "Username is required")
    private String username;

//    @NotBlank(message = "Content must not be empty")
    private String content;
}
