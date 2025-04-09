package com.example.mssscredentials.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer credentialId;
    @Column(unique = true, nullable = false)
    private String token;
    @Column(name = "expires_time", nullable = false)
    private LocalDateTime expiresTime;

    public ResetPasswordToken(Integer credentialId, String token, LocalDateTime expiresTime) {
        this.credentialId = credentialId;
        this.token = token;
        this.expiresTime = expiresTime;
    }

}
