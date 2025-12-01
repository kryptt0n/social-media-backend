package com.example.mssscredentials.controllers;

import com.example.mssscredentials.dto.*;
import com.example.mssscredentials.services.CredentialsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        log.warn("CREDENTIALS TO REGISTER: " + credentials);
        credentialsService.register(credentials.getUsername(), credentials.getPassword(), credentials.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/oauth")
    public ResponseEntity<String> registerOauth(@RequestBody OauthCredentialsRegisterDto credentials) {
        String code = credentialsService.registerOauthCredentials(credentials);
        return ResponseEntity.ok(code);
    }

    @PostMapping("/link/{userId}")
    public ResponseEntity<Void> linkOauth(@RequestBody OauthCredentialsRegisterDto credentials, @PathVariable Integer userId) {
        credentialsService.linkOauth(credentials, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/oauth/{code}")
    public ResponseEntity<Void> registerUsernameOauth(@RequestBody UsernameDto usernameDto, @PathVariable String code) {
        credentialsService.registerUsernameOauth(usernameDto.username(), code);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/oauth")
    public ResponseEntity<Boolean> oauthCredentialsExist(@RequestParam String email,
                                                         @RequestParam String sub,
                                                         @RequestParam String provider) {
        log.warn("Looking for credentials: \nemail:{}\nsub:{}\nprovider:{}", email, sub, provider);
        return ResponseEntity.ok(credentialsService.oauthCredentialsExist(email, sub, provider));
    }

    @GetMapping("/oauth/email/{email}")
    public ResponseEntity<UserShortDTO> getUserByOauthEmail(@PathVariable String email) {
        return ResponseEntity.ok(credentialsService.getUserByOauthEmail(email));
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

    @GetMapping("/username/{userId}")
    public ResponseEntity<String> getUsernameByUserId(@PathVariable Integer userId) {
        Optional<String> username = credentialsService.getUsernameByUserId(userId);

        return username.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deleteCredentialByUsername(@PathVariable String username) {
        credentialsService.deleteCredentialByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/auth-methods/{userId}")
    public ResponseEntity<List<AuthMethodDTO>> authMethods(@PathVariable Integer userId) {
        log.warn("In auth methods! " + userId);
        List<AuthMethodDTO> res = credentialsService.getAuthMethods(userId);
        log.warn(res.toString());
        return ResponseEntity.ok(res);
    }

}
