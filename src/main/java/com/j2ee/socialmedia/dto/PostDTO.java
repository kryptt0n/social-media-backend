package com.j2ee.socialmedia.dto;

import com.j2ee.socialmedia.entities.User;

import java.time.LocalDateTime;

public record PostDTO(Integer id,
                      String content,
                      byte[] image,
                      UserDTO user,
                      LocalDateTime createdAt,
                      boolean likedByCurrentUser,
                      int totalLikes) {
}