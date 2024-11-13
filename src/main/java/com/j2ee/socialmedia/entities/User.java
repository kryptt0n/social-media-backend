package com.j2ee.socialmedia.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;

    @Column(name = "profile_picture")
    private byte[] profilePicture;

    private String bio;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Post> posts;

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Follow> followers;

    @OneToMany(mappedBy = "followed", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Follow> follows;

    public User() {
    }

    public User(Integer id, String username, String password, byte[] profilePicture, String bio, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.profilePicture = profilePicture;
        this.bio = bio;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Follow> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Follow> followers) {
        this.followers = followers;
    }

    public Set<Follow> getFollows() {
        return follows;
    }

    public void setFollows(Set<Follow> follows) {
        this.follows = follows;
    }
}
