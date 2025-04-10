package com.example.msssmediaexchange.entity;

import com.example.msssmediaexchange.dto.Provider;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "media")
@Data
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String sourceId;
    private String s3Key;
    @Enumerated
    private Provider provider;
}
