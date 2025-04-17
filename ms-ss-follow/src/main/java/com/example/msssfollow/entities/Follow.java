package com.example.msssfollow.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "follow")
@Data
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer followerId;
    private Integer followedId;
    private LocalDateTime followedAt;

    public Follow() {}

    public Follow(Integer followerId, Integer followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
        this.followedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getFollowerId() { return followerId; }
    public void setFollowerId(Integer followerId) { this.followerId = followerId; }

    public Integer getFollowedId() { return followedId; }
    public void setFollowedId(Integer followedId) { this.followedId = followedId; }

    public LocalDateTime getFollowedAt() { return followedAt; }
    public void setFollowedAt(LocalDateTime followedAt) { this.followedAt = followedAt; }
}
