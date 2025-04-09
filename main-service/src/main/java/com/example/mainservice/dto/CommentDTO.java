package com.example.mainservice.dto;

import java.time.LocalDateTime;

public record CommentDTO(Integer id,
                         String content,
                         PostDTO post,
                         UserDTO user,
                         LocalDateTime createdAt) {
}
