package com.example.mssscredentials.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class UserIdentifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private IdentifierType type;
    private String value;
    private Integer userId;
    private final LocalDateTime createdAt = LocalDateTime.now();

    public UserIdentifier(IdentifierType type, String value, Integer userId) {
        this.type = type;
        this.value = value;
        this.userId = userId;
    }
}
