package com.socialmedia.msospost.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Integer id;
    private String username;
    private String content;
    private Boolean reported;
    private LocalDateTime createdAt;
}
