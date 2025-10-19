package com.socialmedia.mssspost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {
    private List<PostDto> posts;
    private boolean hasMore;
    private LocalDateTime cursor;
}
