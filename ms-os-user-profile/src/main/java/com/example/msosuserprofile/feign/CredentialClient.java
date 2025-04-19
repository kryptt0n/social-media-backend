package com.example.msosuserprofile.feign;

import com.example.msosuserprofile.dto.CredentialsByUsernameDto;
import com.example.msosuserprofile.dto.CredentialsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-ss-credentials")
public interface CredentialClient {
    @PostMapping("/credentials/register")
    void register(@RequestBody CredentialsDto credentials);
    @PostMapping("/credentials/token")
    Boolean authenticate(@RequestBody CredentialsDto credentials);
    @GetMapping("/credentials/{username}")
    CredentialsByUsernameDto getCredentialsByUsername(@PathVariable String username);
}
