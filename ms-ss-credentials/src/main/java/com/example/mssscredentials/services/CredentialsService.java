package com.example.mssscredentials.services;

import com.example.mssscredentials.entity.Credential;
import com.example.mssscredentials.entity.ResetPasswordToken;
import com.example.mssscredentials.repositories.CredentialRepository;
import com.example.mssscredentials.repositories.ResetPasswordTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CredentialsService {

    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final Sha256Hash hashingService;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final int EXPIRES_IN_HOURS = 1;
    private final EmailService emailService;

    public CredentialsService(CredentialRepository credentialRepository, PasswordEncoder passwordEncoder, Sha256Hash hashingService, ResetPasswordTokenRepository resetPasswordTokenRepository, EmailService emailService) {
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.hashingService = hashingService;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.emailService = emailService;
    }

    public boolean authenticate(String username, String password) {
        Optional<Credential> credential = credentialRepository.findByUsername(username);

        return credential.isPresent() &&
                passwordEncoder.matches(password, credential.get().getPassword());
    }

    public void register(String username, String password, Integer userId) {
        credentialRepository.save(new Credential(username, passwordEncoder.encode(password), userId));
    }

    public void forgotPassword(String email) {
        // TODO: Implement forgot password after UserProfile microservice is ready
        //Optional<User> userOptional = userRepository.findByEmail(email);
//        if (userOptional.isPresent()) {
//            //User user = userOptional.get();
//            Optional<ResetPasswordToken> storedToken = resetPasswordTokenRepository.findByUserId(1);
//            storedToken.ifPresent(resetPasswordTokenRepository::delete);
//
//            ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
//            String token = UUID.randomUUID().toString();
//            resetPasswordToken.setResetPasswordToken(hashingService.hash(token));
//            resetPasswordToken.setUserId(user);
//            resetPasswordToken.setExpiresTime(LocalDateTime.now().plusHours(EXPIRES_IN_HOURS));
//            resetPasswordTokenRepository.save(resetPasswordToken);
//
//            emailService.sendForgotPasswordCode(user.getEmail(), token);
//        } else {
//            throw new RuntimeException("User not found");
//        }
    }

    public void resetPassword(String resetToken, String newPassword) {
        Optional<ResetPasswordToken> resetTokenOptional = resetPasswordTokenRepository.findByToken(hashingService.hash(resetToken));

        if (resetTokenOptional.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }

        ResetPasswordToken resetPasswordToken = resetTokenOptional.get();

        if (resetPasswordToken.getExpiresTime().isBefore(LocalDateTime.now())) {
            resetPasswordTokenRepository.delete(resetPasswordToken);
            throw new RuntimeException("Token has expired");
        }

        Optional<Credential> credentialOptional = credentialRepository.findById(resetPasswordToken.getCredentialId());

        if (credentialOptional.isPresent()) {
            Credential credential = credentialOptional.get();
            credential.setPassword(passwordEncoder.encode(newPassword));
            credentialRepository.save(credential);
        }

        resetPasswordTokenRepository.delete(resetPasswordToken);
    }
}
