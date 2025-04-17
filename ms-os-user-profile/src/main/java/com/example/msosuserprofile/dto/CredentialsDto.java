package com.example.msosuserprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsDto {
    private String username;
    private String password;
    private Integer userId;
}
