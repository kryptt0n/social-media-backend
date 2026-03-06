package com.example.msosuserprofile.feign;

import com.example.msosuserprofile.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "ms-ss-user-profile-crud", dismiss404 = true, path = "/usercrud")
public interface UserCrudClient {

    @PostMapping("/register")
    ResponseEntity<UserProfileDTO> register(@RequestBody UserProfileRegisterDTO user);

    @GetMapping("/users/{userId}")
    Optional<UserProfileDTO> getUser(@PathVariable Integer userId);

    @GetMapping("/usernames/{username}")
    UserShortDTO getUserByUsername(@PathVariable String username);

    @GetMapping("/users/usernames/{username}")
    Optional<UserProfileDTO> getUserProfileByUsername(@PathVariable String username);

    @PostMapping("/deactivate/{userId}")
    void deactivateUser(@PathVariable Integer userId);

    @PutMapping("/update/{userId}")
    String updateUser(@RequestBody UpdateRequestDto dto, @PathVariable Integer userId);

    @PostMapping("/recover/{userId}")
    void recoverUser(@PathVariable Integer userId);

    @PostMapping("/set-public/{userId}")
    void setPublic(@PathVariable Integer userId);

    @PostMapping("/set-private/{userId}")
    void setPrivate(@PathVariable Integer userId);

    @DeleteMapping("/delete/{userId}")
    void deleteUser(@PathVariable Integer userId);

    @DeleteMapping("/delete/email/{email}")
    void deleteUserWithEmail(@PathVariable String email);
}