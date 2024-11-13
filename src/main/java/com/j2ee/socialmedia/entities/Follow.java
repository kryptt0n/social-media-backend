package com.j2ee.socialmedia.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    private User followed;

    @Column(name = "created_at")
    private LocalDateTime followedAt;
}
