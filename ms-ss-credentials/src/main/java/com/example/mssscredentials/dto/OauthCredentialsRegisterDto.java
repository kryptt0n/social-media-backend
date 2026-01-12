package com.example.mssscredentials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OauthCredentialsRegisterDto {
    private String sub;
    private String provider;
    private String email;
    private String pictureUrl;
}
