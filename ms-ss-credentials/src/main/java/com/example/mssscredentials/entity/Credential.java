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
    private String password;
    private Integer userId;

    public Credential(String password, Integer userId) {
        this.password = password;
        this.userId = userId;
    }
}
