package com.example.mssscredentials.entity;

import com.example.mssscredentials.dto.AuthMethodType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthMethodKey {
    private AuthMethodType type;
    private String provider;
}
