package com.socialmedia.msssidentity.service;

import com.socialmedia.msssidentity.model.Credential;
import com.socialmedia.msssidentity.repository.CredentialRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final CredentialRepository credentialRepository;

    public MyUserDetailsService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Credential credential = credentialRepository.findByEmail(email)
            .orElseThrow(() -> {
                System.out.println("❌ User not found: " + email);
                return new UsernameNotFoundException("User not found");
            });

        System.out.println("✅ User found: " + credential.getEmail());
        return User.builder()
                .username(credential.getEmail())
                .password(credential.getPassword())
                .roles("USER")
                .build();
    }
}
