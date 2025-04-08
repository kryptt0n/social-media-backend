package com.socialmedia.mssscredential.controller;

import com.socialmedia.mssscredential.model.UserCredential;
import com.socialmedia.mssscredential.repository.CredentialRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credentials")
public class CredentialController {

    private final CredentialRepository credentialRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CredentialController(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCredential userCredential) {
        System.out.println("📥 Register request for: " + userCredential.getEmail());

        if (credentialRepository.findByEmail(userCredential.getEmail()).isPresent()) {
            System.out.println("⚠️ Email already in use: " + userCredential.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        UserCredential saved = credentialRepository.save(userCredential);

        System.out.println("✅ User registered with ID: " + saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.getId());
    }
}
