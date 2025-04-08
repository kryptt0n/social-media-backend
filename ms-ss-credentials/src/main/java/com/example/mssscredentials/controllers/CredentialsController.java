package com.example.mssscredentials.controllers;

import com.example.mssscredentials.dto.CredentialsDto;
import com.example.mssscredentials.services.CredentialsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credentials")
public class CredentialsController {

    private static final Logger log = LoggerFactory.getLogger(CredentialsController.class);
    private final CredentialsService credentialsService;

    public CredentialsController(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Boolean> authenticate(@RequestBody CredentialsDto credentials) {
        return ResponseEntity.ok(credentialsService.authenticate(credentials.getUsername(), credentials.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody CredentialsDto credentials) {
        log.warn("Attempting to register credentials with username {}", credentials.getUsername());
        credentialsService.register(credentials.getUsername(), credentials.getPassword());
        return ResponseEntity.ok().build();
    }
}
