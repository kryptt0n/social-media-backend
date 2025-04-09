package com.example.mainservice.dto;


public class UpdateUserDTO {
    private String imageUrl;
    private String bio;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(String imageUrl, String bio) {
        this.imageUrl = imageUrl;
        this.bio = bio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
