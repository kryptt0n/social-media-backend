package com.example.mssscredentials.services;

import com.example.mssscredentials.entity.IdentifierType;
import com.example.mssscredentials.exceptions.UserAlreadyExistsException;
import com.example.mssscredentials.feign.MediaClient;
import com.example.mssscredentials.feign.UserCrudClient;
import com.example.mssscredentials.repositories.CredentialRepository;
import com.example.mssscredentials.repositories.UserIdentifierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CredentialServiceIT {

    @Autowired
    private CredentialsService credentialService;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private UserIdentifierRepository userIdentifierRepository;

    @MockitoBean
    private UserCrudClient userCrudClient;

    @MockitoBean
    private MediaClient mediaClient;

    @MockitoBean
    private EmailService emailService;

    @Test
    void register_shouldSaveUser_whenUserDoesNotExist() {
        String username = "testuser";
        String password = "password123";
        Integer userId = 1;

        credentialService.register(username, password, userId);

        var credential = credentialRepository.findByUserId(userId);
        assertTrue(credential.isPresent());
        assertNotNull(credential.get().getPassword());
        assertNotEquals(password, credential.get().getPassword());

        var identifier = userIdentifierRepository.findByIdentifier(username);
        assertTrue(identifier.isPresent());
        assertEquals(IdentifierType.USERNAME, identifier.get().getType());
        assertEquals(userId, identifier.get().getUserId());
    }

    @Test
    void register_shouldThrowException_whenUserAlreadyExists() {
        String username = "existinguser";
        String password = "password123";
        Integer userId = 1;

        credentialService.register(username, password, userId);

        assertThrows(UserAlreadyExistsException.class, () -> credentialService.register(username, password, userId));

    }

}