package com.example.msosidentity.feign;

import com.example.msosidentity.dto.CredentialsDto;
import com.example.msosidentity.dto.ForgotPasswordDTO;
import com.example.msosidentity.dto.ResetPasswordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-ss-credentials")
public interface CredentialClient {
    @PostMapping("/credentials/register")
    void register(@RequestBody CredentialsDto credentials);
    @PostMapping("/credentials/token")
    Boolean authenticate(@RequestBody CredentialsDto credentials);
    @PostMapping("/credentials/forgot-password")
    void forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO);
    @PostMapping("/credentials/reset")
    void resetPassword(@RequestHeader("Authorization") String authHeader, @RequestBody ResetPasswordDTO resetPasswordDTO);
}
