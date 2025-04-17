package com.example.mssscredentials.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordDTO {

    @Email(message = "Should be a valid email")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    public @Email(message = "Should be a valid email") @NotBlank(message = "Email cannot be blank") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Should be a valid email") @NotBlank(message = "Email cannot be blank") String email) {
        this.email = email;
    }
}
