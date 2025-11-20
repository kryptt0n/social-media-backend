package com.example.msosidentity.feign;

import com.example.msosidentity.dto.GenerateTokenDto;
import com.example.msosidentity.dto.JwtKeyDto;
import com.example.msosidentity.dto.TokenValidateDto;
import com.example.msosidentity.dto.UsernameResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-ss-jwt", path = "/jwt")
public interface JwtClient {
    @PostMapping
    JwtKeyDto generateJwt(@RequestBody GenerateTokenDto generateTokenDto);
    @PostMapping("/introspect")
    JwtKeyDto validate(@RequestBody TokenValidateDto tokenValidateDto);

    @GetMapping("/me")
    UsernameResponse extractUsername(@CookieValue(value = "token") String token);
}
