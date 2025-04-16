package com.socialmedia.mssspost.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Kept only for legacy support
    private Long userId;

    private String username;

    private String content;

    @Builder.Default
    private Boolean reported = false;

    private LocalDateTime createdAt;
}
