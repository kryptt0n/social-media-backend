package com.example.msssuserprofilecrud.entities;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String bio;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String roles;

    private String email;

    @Column(name = "is_public")
    private boolean isPublic = true;

    @Column(name = "is_locked")
    private boolean isAccountNonLocked = true;

    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

}
