package com.example.mssspostcrud.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String content;
    private Boolean reported = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}
