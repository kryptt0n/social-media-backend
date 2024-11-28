package com.j2ee.socialmedia.dto;

import com.j2ee.socialmedia.entities.User;

public record PostDTO(Integer id,
                      String content,
                      byte[] image,
                      User user,
                      boolean likedByCurrentUser,
                      int totalLikes) {
}