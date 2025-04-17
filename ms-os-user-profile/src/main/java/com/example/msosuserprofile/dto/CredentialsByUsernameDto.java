package com.example.msosuserprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsByUsernameDto {
    private Boolean exists;
    private String username;
    private Integer userId;
}
