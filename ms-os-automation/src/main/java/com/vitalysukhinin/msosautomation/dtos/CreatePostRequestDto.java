package com.vitalysukhinin.msosautomation.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequestDto {
    @NotNull(message = "Username is required")
    private String username;

//    @NotBlank(message = "Content must not be empty")
    private String content;
}
