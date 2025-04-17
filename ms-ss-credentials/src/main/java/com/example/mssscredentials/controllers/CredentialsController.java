package com.example.mssscredentials.controllers;

import com.example.mssscredentials.dto.CredentialsDto;
import com.example.mssscredentials.dto.CredentialsRegisterDto;
import com.example.mssscredentials.dto.ForgotPasswordDTO;
import com.example.mssscredentials.dto.ResetPasswordDTO;
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
    public ResponseEntity<Boolean> exists(@PathVariable String username) {
        boolean exists = credentialsService.existsByUsername(username);
        return ResponseEntity.ok(exists);
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
}
