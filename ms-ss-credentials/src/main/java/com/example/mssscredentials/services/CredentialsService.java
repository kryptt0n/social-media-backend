package com.example.mssscredentials.services;

import com.example.mssscredentials.dto.*;
import com.example.mssscredentials.entity.*;
import com.example.mssscredentials.exceptions.CodeException;
import com.example.mssscredentials.exceptions.UserAlreadyExistsException;
import com.example.mssscredentials.exceptions.UserNotFoundException;
import com.example.mssscredentials.feign.MediaClient;
import com.example.mssscredentials.feign.UserCrudClient;
import com.example.mssscredentials.repositories.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CredentialsService {

    private static final Logger log = LoggerFactory.getLogger(CredentialsService.class);
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final Sha256Hash hashingService;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final int EXPIRES_IN_HOURS = 1;
    private final EmailService emailService;
    private final UserCrudClient userCrudClient;
    private final AuthIdentityRepository authIdentityRepository;
    private final TemporaryIdentityCredentialsRepository temporaryIdentityCredentialsRepository;
    private final UserIdentifierRepository userIdentifierRepository;
    private final MediaClient mediaClient;

    public CredentialsService(CredentialRepository credentialRepository, PasswordEncoder passwordEncoder, Sha256Hash hashingService, ResetPasswordTokenRepository resetPasswordTokenRepository, EmailService emailService, UserCrudClient userCrudClient, AuthIdentityRepository authIdentityRepository, TemporaryIdentityCredentialsRepository temporaryIdentityCredentialsRepository, UserIdentifierRepository userIdentifierRepository, MediaClient mediaClient) {
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.hashingService = hashingService;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.emailService = emailService;
        this.userCrudClient = userCrudClient;
        this.authIdentityRepository = authIdentityRepository;
        this.temporaryIdentityCredentialsRepository = temporaryIdentityCredentialsRepository;
        this.userIdentifierRepository = userIdentifierRepository;
        this.mediaClient = mediaClient;
    }

    public boolean authenticate(String username, String password) {
        Optional<UserIdentifier> userIdentifier = userIdentifierRepository.findByValue(username);

        if (userIdentifier.isEmpty())
            return false;

        Optional<Credential> credential = credentialRepository.findByUserId(userIdentifier.get().getUserId());

        return  credential.isPresent() &&
                passwordEncoder.matches(password, credential.get().getPassword());
    }

    public void register(String username, String password, Integer userId) {
        if (credentialRepository.existsByUserId(userId)) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
        credentialRepository.save(new Credential(passwordEncoder.encode(password), userId));
        userIdentifierRepository.save(new UserIdentifier(IdentifierType.USERNAME, username, userId));
    }

    public String registerOauthCredentials(OauthCredentialsRegisterDto credentials) {

        if (oauthCredentialsExist(credentials.getEmail(), credentials.getProvider(), credentials.getSub())) {
            throw new UserAlreadyExistsException("User already registered");
        }
        String code = generateTempOauthCode();

        TemporaryIdentityCredentials tempCreds = new TemporaryIdentityCredentials();
        tempCreds.setEmail(credentials.getEmail());
        tempCreds.setProvider(credentials.getProvider());
        tempCreds.setSub(credentials.getSub());
        tempCreds.setPictureUrl(credentials.getPictureUrl());
        tempCreds.setCode(code);
        temporaryIdentityCredentialsRepository.save(tempCreds);

        return code;
    }

    public void linkOauth(OauthCredentialsRegisterDto credentials, Integer userId) {

        if (oauthCredentialsExist(credentials.getEmail(), credentials.getProvider(), credentials.getSub())) {
            throw new UserAlreadyExistsException("User already registered");
        }

        //TODO: If user does not exist with this id do not add

        AuthIdentity authIdentity = new AuthIdentity(AuthMethodType.OAUTH, credentials.getSub(), credentials.getProvider(), credentials.getEmail(), userId);
        authIdentityRepository.save(authIdentity);

    }

    public boolean oauthCredentialsExist(String email, String provider, String sub) {
        List<AuthIdentity> identities = authIdentityRepository.findAllByTypeAndValue(AuthMethodType.OAUTH, email);
        return identities.stream().anyMatch(authIdentity ->
                authIdentity.getProvider().equals(provider) &&
                        authIdentity.getSub().equals(sub));

    }

    public void forgotPassword(String email) {

        Optional<UserShortDTO> userProfileOptional = userCrudClient.getUserByEmail(email);
        if (userProfileOptional.isPresent()) {
            UserShortDTO userProfile = userProfileOptional.get();
            Credential credential = credentialRepository.findByUserId(userProfile.userId()).orElseThrow();

            Optional<ResetPasswordToken> storedToken = resetPasswordTokenRepository.findByCredentialId(credential.getId());
            storedToken.ifPresent(resetPasswordTokenRepository::delete);

            ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
            String token = UUID.randomUUID().toString();
            resetPasswordToken.setToken(hashingService.hash(token));
            resetPasswordToken.setCredentialId(credential.getId());
            resetPasswordToken.setExpiresTime(LocalDateTime.now().plusHours(EXPIRES_IN_HOURS));
            resetPasswordTokenRepository.save(resetPasswordToken);

            emailService.sendForgotPasswordCode(userProfile.email(), token);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public void resetPassword(String resetToken, String newPassword) {
        Optional<ResetPasswordToken> resetTokenOptional = resetPasswordTokenRepository.findByToken(hashingService.hash(resetToken));

        if (resetTokenOptional.isEmpty()) {
            throw new CodeException("Invalid token");
        }

        ResetPasswordToken resetPasswordToken = resetTokenOptional.get();

        if (resetPasswordToken.getExpiresTime().isBefore(LocalDateTime.now())) {
            resetPasswordTokenRepository.delete(resetPasswordToken);
            throw new CodeException("Token has expired");
        }

        Optional<Credential> credentialOptional = credentialRepository.findById(resetPasswordToken.getCredentialId());

        if (credentialOptional.isPresent()) {
            Credential credential = credentialOptional.get();
            credential.setPassword(passwordEncoder.encode(newPassword));
            credentialRepository.save(credential);
        }

        resetPasswordTokenRepository.delete(resetPasswordToken);
    }

    //TODO: GET USERNAME BOTH FROM CREDS AND OAUTH

    public Optional<String> getUsernameByUserId(Integer userId) {
        return userIdentifierRepository.findByUserIdAndType(userId, IdentifierType.USERNAME).map(UserIdentifier::getValue);
    }

    public UserShortDTO getUserByOauthEmail(String email) {
        List<AuthIdentity> identities = authIdentityRepository.findAllByTypeAndValue(AuthMethodType.OAUTH, email);

        if (identities.isEmpty())
            throw new UserNotFoundException("User is not found");

        AuthIdentity identity = identities.getFirst();

        Optional<UserIdentifier> identifier = userIdentifierRepository.findByUserIdAndType(identity.getUserId(), IdentifierType.USERNAME);

        if (identifier.isEmpty())
            throw new UserNotFoundException("User is not found");

        return new UserShortDTO(true, email, identifier.get().getValue(), identity.getUserId());

    }

    @Transactional
    public void deleteCredentialByUsername(String username) {
        Optional<UserIdentifier> identifier = userIdentifierRepository.findByValue(username);
        if (identifier.isPresent()) {
            credentialRepository.deleteAllByUserId(identifier.get().getUserId());
            userIdentifierRepository.delete(identifier.get());
        }
    }

    public void registerUsernameOauth(String username, String code) {

        Optional<TemporaryIdentityCredentials> tempCredsOptional = temporaryIdentityCredentialsRepository.findByCode(code);
        if (tempCredsOptional.isPresent()) {
            TemporaryIdentityCredentials tempCreds = tempCredsOptional.get();
            Optional<UserShortDTO> userShortEmailOptional = userCrudClient.getUserByEmail(tempCreds.getEmail());
            if (userShortEmailOptional.isPresent())
                throw new UserAlreadyExistsException("User is already registered. Please log in." );

            Optional<UserShortDTO> userShortUsernameOptional = userCrudClient.getUserByUsername(username);

            if (userShortUsernameOptional.isPresent())
                throw new UserAlreadyExistsException("User with this username already exists." );


            UserProfileDTO savedUser = userCrudClient.register(new UserProfileRegisterDTO(tempCreds.getEmail(), username, "", true));
            mediaClient.save(new MediaPayload(savedUser.id().toString(), tempCreds.getPictureUrl(), Provider.PROFILE, ImageType.URL));
            AuthIdentity authIdentity = new AuthIdentity(AuthMethodType.OAUTH, tempCreds.getSub(), tempCreds.getProvider(), tempCreds.getEmail(), savedUser.id());
            authIdentityRepository.save(authIdentity);

        } else {
            throw new CodeException("Incorrect code");
        }

    }

    public List<AuthMethodDTO> getAuthMethods(Integer userId) {
        List<AuthMethodDTO> result = new ArrayList<>();
        List<AuthMethodDTO> identities = authIdentityRepository
                .findAllByUserId(userId)
                .stream()
                .map(identity ->
                        new AuthMethodDTO(AuthMethodType.OAUTH,
                                identity.getProvider(),
                                true,
                                false,
                                false
                                ))
                .toList();

        List<AuthMethodDTO> identifiers = userIdentifierRepository
                .findAllByUserId(userId)
                .stream()
                .map(identifier ->
                        new AuthMethodDTO(AuthMethodType.PASSWORD,
                                null,
                                true,
                                false,
                                false))
                .toList();

        result.addAll(identities);
        result.addAll(identifiers);

        return mergeWithAllMethods(result);
    }

    private List<AuthMethodDTO> mergeWithAllMethods(List<AuthMethodDTO> existingMethods) {

        List<AuthMethodKey> availableMethods = allAvailableMethods();

        Map<AuthMethodKey, AuthMethodDTO> existingMap = existingMethods
                .stream()
                .collect(Collectors.toMap(
                        method -> new AuthMethodKey(method.getType(), method.getProvider()),
                        Function.identity()
                        )
                );

        for (var method : existingMap.keySet()) {
            availableMethods.removeIf(availableMethod -> availableMethod.equals(method));
        }

        ArrayList<AuthMethodDTO> result = new ArrayList<>(existingMethods);

        for (var method: availableMethods) {
            result.add(new AuthMethodDTO(method.getType(), method.getProvider(), false, true, false));
        }


        return result;
    }

    private List<AuthMethodKey> allAvailableMethods() {
        return new ArrayList<>(
                List.of(new AuthMethodKey(AuthMethodType.PASSWORD, null),
                        new AuthMethodKey(AuthMethodType.OAUTH, "google-oauth2"))
        );
    }

    private String generateTempOauthCode() {
        return UUID.randomUUID().toString();
    }

}
