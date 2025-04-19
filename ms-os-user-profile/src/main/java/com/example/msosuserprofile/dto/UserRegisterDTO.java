package com.example.msosuserprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    private String username;
    private String password;
    private String email;
    private String bio;
    private Boolean isPublic;
    private String base64Image;
}
