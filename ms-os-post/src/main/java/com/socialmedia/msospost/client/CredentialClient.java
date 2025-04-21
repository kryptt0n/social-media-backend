package com.socialmedia.msospost.client;

import com.socialmedia.msospost.dto.CredentialsByUsernameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-ss-credentials", path = "/credentials")
public interface CredentialClient {
    @GetMapping("/{username}")
    CredentialsByUsernameDto getCredentialsByUsername(@PathVariable("username") String username);
}