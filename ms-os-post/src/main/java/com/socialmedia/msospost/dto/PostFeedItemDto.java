package com.socialmedia.msospost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostFeedItemDto {
    private Integer postId;
    private String username;
    private String content;
    private String imageUrl;
    private String avatarUrl;
    private Integer likeCount;
    private Boolean likedByCurrentUser;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> comments;
}
