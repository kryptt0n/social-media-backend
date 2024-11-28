package com.j2ee.socialmedia.dto;

public record UserDTO(String username, byte[] profilePicture, String bio, boolean isFollowed) {
}
