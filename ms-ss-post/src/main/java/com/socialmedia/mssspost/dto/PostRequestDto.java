package com.socialmedia.mssspost.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostRequestDto {
    @NotBlank
    private String content;
    private String imageUrl;
    @NotNull
    private Integer userId;
}

