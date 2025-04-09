package com.example.mssscredentials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsRegisterDto {
    private String username;
    private String password;
    private Integer userId;
}
