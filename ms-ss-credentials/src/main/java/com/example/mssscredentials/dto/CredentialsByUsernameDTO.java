package com.example.mssscredentials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsByUsernameDTO {
    private Boolean exists;
    private String username;
    private Integer userId;
}
