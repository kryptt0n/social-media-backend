package com.socialmedia.msospost.dto;

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
public class PostFeedResponseDto {
    private List<PostFeedItemDto> posts;
    private boolean hasMore;
    private LocalDateTime cursor;
}
