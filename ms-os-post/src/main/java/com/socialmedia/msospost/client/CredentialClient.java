package com.socialmedia.msospost.client;

import com.socialmedia.msospost.dto.CredentialsByUsernameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-ss-credentials")
public interface CredentialClient {
    @GetMapping("/credentials/{username}")
    CredentialsByUsernameDto getCredentialsByUsername(@PathVariable String username);
}
