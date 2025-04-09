package com.example.mainservice.dto;

import java.time.LocalDateTime;

public record PostDTO(Integer id,
                      String content,
                      String imageUrl,
                      UserDTO user,
                      LocalDateTime createdAt,
                      boolean likedByCurrentUser,
                      int totalLikes,
                      boolean reported) {
}