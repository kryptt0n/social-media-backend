package com.example.mssscredentials.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class TemporaryIdentityCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sub;
    private String provider;
    private String email;
    private String pictureUrl;
    private String code;
    private static int EXPIRES_IN_HOURS = 1;
    private LocalDateTime expiresTime = LocalDateTime.now().plusHours(EXPIRES_IN_HOURS);


    public TemporaryIdentityCredentials(String sub, String provider, String email, String pictureUrl, String code) {
        this.sub = sub;
        this.provider = provider;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.code = code;
    }
}
