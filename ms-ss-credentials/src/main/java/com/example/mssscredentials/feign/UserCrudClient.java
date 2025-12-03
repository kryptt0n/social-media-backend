package com.example.mssscredentials.feign;

import com.example.mssscredentials.dto.UserShortDTO;
import com.example.mssscredentials.dto.UserProfileDTO;
import com.example.mssscredentials.dto.UserProfileRegisterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "ms-ss-user-profile-crud", dismiss404 = true, path = "/usercrud")
public interface UserCrudClient {
    @GetMapping("/emails/{email}")
    Optional<UserShortDTO> getUserByEmail(@PathVariable("email") String email);

    @GetMapping("/usernames/{username}")
    Optional<UserShortDTO> getUserByUsername(@PathVariable("username") String username);

    @PostMapping("/register")
    UserProfileDTO register(@RequestBody UserProfileRegisterDTO user);

    @DeleteMapping("/delete/email/{email}")
    void deleteUserWithEmail(@PathVariable String email);
}
