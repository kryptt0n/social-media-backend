package com.example.msosidentity.feign;

import com.example.msosidentity.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "ms-ss-credentials", dismiss404 = true, path = "/credentials")
public interface CredentialClient {
    @PostMapping("/register")
    void register(@RequestBody CredentialsDto credentials);
    @PostMapping("/token")
    Boolean authenticate(@RequestBody CredentialsDto credentials);
    @PostMapping("/forgot-password")
    void forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO);
    @PostMapping("/reset")
    void resetPassword(@RequestHeader("Authorization") String authHeader, @RequestBody ResetPasswordDTO resetPasswordDTO);
    @GetMapping("/oauth")
    Boolean oauthCredentialsExist(@RequestParam String email, @RequestParam String sub, @RequestParam String provider);
    @PostMapping("/oauth")
    String registerOauthCredentials(@RequestBody OauthCredentialsRegisterDto credentials);
    @PostMapping("/oauth/{code}")
    void registerUsernameOauth(@RequestBody UsernameResponse usernameDto, @PathVariable String code);
    @GetMapping("/auth-methods/{userId}")
    List<AuthMethodDTO> authMethods(@PathVariable Integer userId);
    @PostMapping("/link/{userId}")
    void linkOauth(@RequestBody OauthCredentialsRegisterDto credentials, @PathVariable Integer userId);
    @GetMapping("/oauth/email/{email}")
    Optional<UserShortDTO> getUserByOauthEmail(@PathVariable String email);
}
