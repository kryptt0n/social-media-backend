package com.example.msssfollow.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "follow")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer followerId;
    private Integer followedId;
    private LocalDateTime followedAt = LocalDateTime.now();
}
