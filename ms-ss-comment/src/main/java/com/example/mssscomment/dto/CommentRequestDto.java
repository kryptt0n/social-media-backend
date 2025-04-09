package com.example.mssscomment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    @NotNull
    private String username;
    @NotNull
    private Integer postId;
    @NotNull
    private String content;
}
