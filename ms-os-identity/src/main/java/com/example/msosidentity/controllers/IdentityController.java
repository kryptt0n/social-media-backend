package com.example.msosidentity.controllers;

import com.example.msosidentity.dto.*;
import com.example.msosidentity.feign.CredentialClient;
import com.example.msosidentity.feign.JwtClient;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity")
public class IdentityController {

    private final JwtClient jwtClient;
    private final CredentialClient credentialClient;

    public IdentityController(JwtClient jwtClient, CredentialClient credentialClient) {
        this.jwtClient = jwtClient;
        this.credentialClient = credentialClient;
    }

    @PostMapping("/token")
    public ResponseEntity<JwtKeyDto> login(@RequestBody CredentialsDto credentials) {
        if (credentialClient.authenticate(credentials)) {
            JwtKeyDto jwtKeyDto = jwtClient.generateJwt(new GenerateTokenDto("test"));
            return ResponseEntity.ok(jwtKeyDto);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<JwtKeyDto> validateJwtToken(@RequestBody TokenValidateDto tokenValidateDto) {
        JwtKeyDto response = jwtClient.validate(tokenValidateDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtKeyDto> register(@RequestBody CredentialsDto credentials) {
        credentialClient.register(credentials);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        credentialClient.forgotPassword(forgotPasswordDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestHeader("Authorization") String authHeader,
                                                @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        credentialClient.resetPassword(authHeader, resetPasswordDTO);
        return ResponseEntity.ok("Password reset");
    }
}
