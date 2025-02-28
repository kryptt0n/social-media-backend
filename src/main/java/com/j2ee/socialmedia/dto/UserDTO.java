package com.j2ee.socialmedia.dto;

public record UserDTO(String username,
                      String imageUrl,
                      String bio,
                      boolean isFollowed,
                      Integer followersCount,
                      Integer followingCount,
                      boolean isActive,
                      boolean isPublic) {
}
