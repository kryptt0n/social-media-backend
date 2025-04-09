package com.example.msosidentity.feign;

import com.example.msosidentity.dto.GenerateTokenDto;
import com.example.msosidentity.dto.JwtKeyDto;
import com.example.msosidentity.dto.TokenValidateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-ss-jwt")
public interface JwtClient {
    @PostMapping("/jwt")
    JwtKeyDto generateJwt(@RequestBody GenerateTokenDto generateTokenDto);
    @PostMapping("/jwt/introspect")
    JwtKeyDto validate(@RequestBody TokenValidateDto tokenValidateDto);
}
