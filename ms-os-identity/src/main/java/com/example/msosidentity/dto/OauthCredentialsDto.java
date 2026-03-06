package com.example.msosidentity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OauthCredentialsDto {
    private String email;
    private String name;
    private String sub;
    private String picture;
}
