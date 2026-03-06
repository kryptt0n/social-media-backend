package com.example.msosidentity.controllers;

import com.example.msosidentity.dto.*;
import com.example.msosidentity.exceptions.InvalidCredentialsException;
import com.example.msosidentity.feign.CredentialClient;
import com.example.msosidentity.feign.JwtClient;
import com.example.msosidentity.feign.UserProfileClient;
import com.example.msosidentity.services.ControllersUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/identity")
public class IdentityController {

    private static final Logger log = LoggerFactory.getLogger(IdentityController.class);
    private final JwtClient jwtClient;
    private final CredentialClient credentialClient;
    private final Integer TOKEN_MAX_AGE = 1 * 60 * 60;  // 1 hour
    private final UserProfileClient userProfileClient;
    private final ControllersUtil controllersUtil;

    public IdentityController(JwtClient jwtClient, CredentialClient credentialClient, UserProfileClient userProfileClient, ControllersUtil controllersUtil) {
        this.jwtClient = jwtClient;
        this.credentialClient = credentialClient;
        this.userProfileClient = userProfileClient;
        this.controllersUtil = controllersUtil;
    }

    @PostMapping("/token")
    public ResponseEntity<JwtKeyDto> login(@RequestBody CredentialsDto credentials) {
        if (credentialClient.authenticate(credentials)) {
            JwtKeyDto jwtKeyDto = jwtClient.generateJwt(new GenerateTokenDto(credentials.getUsername()));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, configureJwtCookie(jwtKeyDto.getKey()).toString());
            return ResponseEntity.ok().headers(headers).body(jwtKeyDto);
        }
        throw new InvalidCredentialsException("Invalid username or password");
    }

    @PostMapping("/validate")
    public ResponseEntity<JwtKeyDto> validateJwtToken(@CookieValue(value = "token") String token,
                                                      @RequestBody UsernameResponse username) {
        log.warn("Token: {}, Username: {}", token, username);
        JwtKeyDto response = jwtClient.validate(new TokenValidateDto(token));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtKeyDto> register(@RequestBody CredentialsDto credentials) {
        credentialClient.register(credentials);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        credentialClient.forgotPassword(forgotPasswordDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestHeader("Authorization") String authHeader,
                                                @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        credentialClient.resetPassword(authHeader, resetPasswordDTO);
        return ResponseEntity.ok("Password reset");
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeDTO> extractUsername(@CookieValue(value = "token") String token) {
        log.warn("Token at /me : {}", token);
        UsernameResponse response = jwtClient.extractUsername(token);
        UserShortDTO userShortDTOResponse = userProfileClient.getUserByUsername(response.username());
        log.warn("Username after /me : {} and userId: {}", userShortDTOResponse.username(), userShortDTOResponse.userId());
        return ResponseEntity.ok(new UserMeDTO(userShortDTOResponse.username(), userShortDTOResponse.userId()));
    }

    @PostMapping("/set-password")
    public ResponseEntity<Void> setPassword(@RequestBody SetPasswordDto setPasswordDto, Authentication authentication) {

        UserMeDTO user = controllersUtil.extractUser(authentication);
        credentialClient.register(new CredentialsDto(user.username(), setPasswordDto.getPassword(), user.userId()));
        return ResponseEntity.ok().build();

    }

    private ResponseCookie configureJwtCookie(String jwtToken) {
        return ResponseCookie.from("token", jwtToken)
                .httpOnly(true)
                .path("/")
                .maxAge(TOKEN_MAX_AGE)
                .build();
    }

}
