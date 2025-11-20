package com.example.msosidentity.feign;

import com.example.msosidentity.dto.UserShortDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "ms-ss-user-profile-crud", path = "/usercrud", dismiss404 = true)
public interface UserProfileClient {

    @GetMapping("/emails/{email}")
    Optional<UserShortDTO> getUserByEmail(@PathVariable String email);
    @GetMapping("/usernames/{username}")
    UserShortDTO getUserByUsername(@PathVariable String username);
}
