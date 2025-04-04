package com.example.mainservice.services;

import com.example.mainservice.entities.ResetPasswordToken;
import com.example.mainservice.entities.User;
import com.example.mainservice.repositories.ResetPasswordTokenRepository;
import com.example.mainservice.repositories.UserRepository;
import com.example.mainservice.services.hashing.HashingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordService {

    private final UserRepository userRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final HashingService hashingService;
    private final PasswordEncoder passwordEncoder;


    private final int EXPIRES_IN_HOURS = 1;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(PasswordService.class);

    public PasswordService(UserRepository userRepository,
                           ResetPasswordTokenRepository resetPasswordTokenRepository,
                           HashingService hashingService,
                           PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.hashingService = hashingService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<ResetPasswordToken> storedToken = resetPasswordTokenRepository.findByUser(user);
            storedToken.ifPresent(resetPasswordTokenRepository::delete);

            ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
            String token = UUID.randomUUID().toString();
            resetPasswordToken.setResetPasswordToken(hashingService.hash(token));
            resetPasswordToken.setUser(user);
            resetPasswordToken.setExpiresTime(LocalDateTime.now().plusHours(EXPIRES_IN_HOURS));
            resetPasswordTokenRepository.save(resetPasswordToken);

            emailService.sendForgotPasswordCode(user.getEmail(), token);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resetPassword(String resetToken, String newPassword) {
        Optional<ResetPasswordToken> resetTokenOptional = resetPasswordTokenRepository.findByResetPasswordToken(hashingService.hash(resetToken));

        if (resetTokenOptional.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }

        ResetPasswordToken resetPasswordToken = resetTokenOptional.get();

        if (resetPasswordToken.getExpiresTime().isBefore(LocalDateTime.now())) {
            resetPasswordTokenRepository.delete(resetPasswordToken);
            throw new RuntimeException("Token has expired");
        }

        User user = resetPasswordToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetPasswordTokenRepository.delete(resetPasswordToken);
    }
}
