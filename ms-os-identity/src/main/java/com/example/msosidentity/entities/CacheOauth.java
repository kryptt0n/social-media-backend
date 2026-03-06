package com.example.msosidentity.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash("oauth")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheOauth {
    @Id
    private String id;
    private OauthAction action;
    private String provider;
    private Integer userId;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

    public CacheOauth(String id, OauthAction action, String provider, Integer userId) {
        this.id = id;
        this.action = action;
        this.provider = provider;
        this.userId = userId;
    }
}
