package com.example.msosidentity.feign;

import com.example.msosidentity.dto.CredentialsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-ss-credentials")
public interface CredentialClient {
    @PostMapping("/credentials/register")
    void register(@RequestBody CredentialsDto credentials);
    @PostMapping("/credentials/token")
    Boolean authenticate(@RequestBody CredentialsDto credentials);
}
