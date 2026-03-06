package com.example.mssscredentials.entity;

import com.example.mssscredentials.dto.AuthMethodType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth_identities")
@Data
@NoArgsConstructor
public class AuthIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AuthMethodType type;
    private String sub;
    private String provider;
    private String value;
    private Integer userId;

    public AuthIdentity(AuthMethodType type, String sub, String provider, String value, Integer userId) {
        this.type = type;
        this.sub = sub;
        this.provider = provider;
        this.value = value;
        this.userId = userId;
    }

}
