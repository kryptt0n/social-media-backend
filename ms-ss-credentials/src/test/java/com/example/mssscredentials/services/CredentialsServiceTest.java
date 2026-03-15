package com.example.mssscredentials.services;

import com.example.mssscredentials.dto.CredentialsRegisterDto;
import com.example.mssscredentials.entity.Credential;
import com.example.mssscredentials.entity.IdentifierType;
import com.example.mssscredentials.entity.UserIdentifier;
import com.example.mssscredentials.exceptions.UserAlreadyExistsException;
import com.example.mssscredentials.feign.MediaClient;
import com.example.mssscredentials.feign.UserCrudClient;
import com.example.mssscredentials.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CredentialsServiceTest {

    @Mock
    private CredentialRepository credentialRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Sha256Hash hashingService;
    @Mock
    private ResetPasswordTokenRepository resetPasswordTokenRepository;
    private int EXPIRES_IN_HOURS = 1;
    @Mock
    private EmailService emailService;
    @Mock
    private UserCrudClient userCrudClient;
    @Mock
    private AuthIdentityRepository authIdentityRepository;
    @Mock
    private TemporaryIdentityCredentialsRepository temporaryIdentityCredentialsRepository;
    @Mock
    private UserIdentifierRepository userIdentifierRepository;
    @Mock
    private MediaClient mediaClient;

    @InjectMocks
    private CredentialsService credentialsService;

    @Test
    void register_shouldSaveUser_whenUserDoesNotExist() {
        CredentialsRegisterDto request = new CredentialsRegisterDto("test", "test", 1);

        when(credentialRepository.existsByUserId(1)).thenReturn(false);
        when(passwordEncoder.encode("test")).thenReturn("encodedPassword");

        credentialsService.register(request.getUsername(), request.getPassword(), request.getUserId());

        verify(credentialRepository).save(any(Credential.class));
        verify(userIdentifierRepository).save(any(UserIdentifier.class));
        verify(passwordEncoder).encode("test");
    }

    @Test
    void register_shouldThrowException_whenUserAlreadyExists() {
        CredentialsRegisterDto request = new CredentialsRegisterDto("test", "test", 1);

        when(credentialRepository.existsByUserId(1)).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            credentialsService.register(request.getUsername(), request.getPassword(), request.getUserId());
        });

        assertEquals("User with this username already exists", exception.getMessage());
        verify(credentialRepository, never()).save(any(Credential.class));
    }

    @Test
    void authenticate_shouldReturnTrue_whenCredentialsAreValid() {
        String username = "test";
        String password = "test";

        when(userIdentifierRepository.findByIdentifier(username)).thenReturn(Optional.of(new UserIdentifier(IdentifierType.USERNAME, username, 1)));
        when(credentialRepository.findByUserId(1)).thenReturn(Optional.of(new Credential(password, 1)));
        when(passwordEncoder.matches(password, password)).thenReturn(true);

        boolean isAuthenticated = credentialsService.authenticate(username, password);

        assertTrue(isAuthenticated);

    }

    @Test
    void authenticate_shouldReturnFalse_whenUserIdentifierNotFound() {
        String username = "test";
        String password = "test";

        when(userIdentifierRepository.findByIdentifier(username)).thenReturn(Optional.empty());

        boolean isAuthenticated = credentialsService.authenticate(username, password);

        assertFalse(isAuthenticated);
        verify(credentialRepository, never()).findByUserId(anyInt());
    }

    @Test
    void authenticate_shouldReturnFalse_whenPasswordIsIncorrect() {
        String username = "test";
        String password = "test";
        String incorrectPassword = "wrong";

        when(userIdentifierRepository.findByIdentifier(username)).thenReturn(Optional.of(new UserIdentifier(IdentifierType.USERNAME, username, 1)));
        when(credentialRepository.findByUserId(1)).thenReturn(Optional.of(new Credential(password, 1)));
        when(passwordEncoder.matches(incorrectPassword, password)).thenReturn(false);

        boolean isAuthenticated = credentialsService.authenticate(username, incorrectPassword);

        assertFalse(isAuthenticated);
    }
}
