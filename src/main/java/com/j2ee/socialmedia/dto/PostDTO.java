package com.j2ee.socialmedia.dto;

import com.j2ee.socialmedia.entities.User;

import java.time.LocalDateTime;

public record PostDTO(Integer id,
                      String content,
                      byte[] image,
                      User user,
                      LocalDateTime createdAt,
                      boolean likedByCurrentUser,
                      int totalLikes) {
}