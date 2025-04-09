package com.example.mainservice.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(unique = true, nullable = false)
    private String resetPasswordToken;
    @Column(name = "expires_time", nullable = false)
    private LocalDateTime expiresTime;

    public ResetPasswordToken(User user, String resetPasswordToken, LocalDateTime expiresTime) {
        this.user = user;
        this.resetPasswordToken = resetPasswordToken;
        this.expiresTime = expiresTime;
    }

    public ResetPasswordToken() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public LocalDateTime getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(LocalDateTime expiresTime) {
        this.expiresTime = expiresTime;
    }
}
