package com.socialmedia.msospost.dto;

import lombok.Data;

@Data
public class CommentRequestDto {
    private String username;
    private String content;
    private Integer postId;
}
