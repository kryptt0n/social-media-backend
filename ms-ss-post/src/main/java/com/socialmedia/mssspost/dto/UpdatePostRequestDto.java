package com.socialmedia.mssspost.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePostRequestDto {

    @NotBlank(message = "Content must not be empty")
    private String content;
}
