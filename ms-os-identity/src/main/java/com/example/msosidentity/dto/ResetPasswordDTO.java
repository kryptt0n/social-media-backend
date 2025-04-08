package com.example.msosidentity.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordDTO {

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password should contain at least 8 characters")
    private String newPassword;

    public @NotBlank(message = "New password is required") @Size(min = 8, message = "Password should contain at least 8 characters") String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank(message = "New password is required") @Size(min = 8, message = "Password should contain at least 8 characters") String newPassword) {
        this.newPassword = newPassword;
    }
}
