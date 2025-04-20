package com.example.msosadmin.feign;


import com.example.msosadmin.dto.UsernameResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-ss-credentials")
public interface CredentialClient {

    @GetMapping("/credentials/username/{userId}")
    UsernameResponse getUsernameByUserId(@PathVariable Integer userId);
}

