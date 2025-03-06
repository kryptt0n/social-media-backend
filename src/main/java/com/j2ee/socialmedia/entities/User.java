package com.j2ee.socialmedia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(name = "image_url")
    private String imageUrl;

    private String bio;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String roles;

    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "follower", fetch = FetchType.EAGER)
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "followed", fetch = FetchType.EAGER)
    private Set<Follow> follows = new HashSet<>();

    @Column(name = "is_public")
    private boolean isPublic = true;

    @Column(name = "is_locked")
    private boolean isAccountNonLocked = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

}
