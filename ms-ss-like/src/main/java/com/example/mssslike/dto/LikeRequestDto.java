package com.example.mssslike.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LikeRequestDto {
    @NotNull
    private String username;
    @NotNull
    private Integer postId;
}
