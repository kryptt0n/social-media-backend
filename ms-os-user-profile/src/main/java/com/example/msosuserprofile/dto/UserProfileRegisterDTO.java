package com.example.msosuserprofile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRegisterDTO {
    private String email;
    private String bio;
    private Boolean isPublic;
}
