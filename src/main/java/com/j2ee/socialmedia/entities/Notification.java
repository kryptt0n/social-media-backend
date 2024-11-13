package com.j2ee.socialmedia.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private NotificationType type;
    private String content;
    @Column(name = "is_read")
    private Boolean read;
    private LocalDateTime createdAt;
}
