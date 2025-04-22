package com.example.mssscredentials.controllers;

import com.example.mssscredentials.dto.*;
import com.example.mssscredentials.services.CredentialsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credentials")
public class CredentialsController {

    private static final Logger log = LoggerFactory.getLogger(CredentialsController.class);
    private final CredentialsService credentialsService;

    public CredentialsController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @PostMapping("/token")
    public ResponseEntity<Boolean> authenticate(@RequestBody CredentialsDto credentials) {
        return ResponseEntity.ok(credentialsService.authenticate(credentials.getUsername(), credentials.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody CredentialsRegisterDto credentials) {
        credentialsService.register(credentials.getUsername(), credentials.getPassword(), credentials.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<CredentialsByUsernameDTO> getCredentialsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(credentialsService.getCredentialsByUsername(username));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        credentialsService.forgotPassword(forgotPasswordDTO.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(@RequestHeader("Authorization") String authHeader,
                                                @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        String token = authHeader.replace("Bearer ", "");
        credentialsService.resetPassword(token, resetPasswordDTO.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/username/{userId}")
    public ResponseEntity<UsernameResponse> getUsernameByUserId(@PathVariable Integer userId) {
        String username = credentialsService.getUsernameByUserId(userId);
        if (username == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new UsernameResponse(username));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deleteCredentialByUsername(@PathVariable String username) {
        credentialsService.deleteCredentialByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
