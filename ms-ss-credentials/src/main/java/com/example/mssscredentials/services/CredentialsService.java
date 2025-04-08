package com.example.mssscredentials.services;

import com.example.mssscredentials.entity.Credential;
import com.example.mssscredentials.repositories.CredentialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialsService {

    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;

    public CredentialsService(CredentialRepository credentialRepository, PasswordEncoder passwordEncoder) {
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String username, String password) {
        Optional<Credential> credential = credentialRepository.findByUsername(username);

        return credential.isPresent() &&
                passwordEncoder.matches(password, credential.get().getPassword());
    }

    public void register(String username, String password) {
        credentialRepository.save(new Credential(username, passwordEncoder.encode(password)));
    }
}
