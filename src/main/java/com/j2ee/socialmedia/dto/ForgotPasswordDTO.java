package com.j2ee.socialmedia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//@Getter
//@Setter
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
