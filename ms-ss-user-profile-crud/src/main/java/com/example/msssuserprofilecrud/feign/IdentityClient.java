package com.example.msssuserprofilecrud.feign;

import com.example.msssuserprofilecrud.dto.CredentialsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-os-identity")
public interface IdentityClient {

    @PostMapping("/identity/register")
    void register(@RequestBody CredentialsDto credentialsDto);
}