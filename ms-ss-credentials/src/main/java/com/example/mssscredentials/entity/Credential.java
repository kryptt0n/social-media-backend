package com.example.mssscredentials.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credentials")
@Data
@NoArgsConstructor
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private Integer userId;

    public Credential(String username, String password, Integer userId) {
        this.username = username;
        this.password = password;
        this.userId = userId;
    }
}
