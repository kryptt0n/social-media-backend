package com.example.msosuserprofile.controllers;

import com.example.msosuserprofile.dto.*;
import com.example.msosuserprofile.feign.CredentialClient;
import com.example.msosuserprofile.feign.UserCrudClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserProfileController {
    private static final Logger log =  LoggerFactory.getLogger(UserProfileController.class);

    private final UserCrudClient userCrudClient;
    private final CredentialClient credentialClient;

    public UserProfileController(UserCrudClient userCrudClient, CredentialClient credentialClient) {
        this.userCrudClient = userCrudClient;
        this.credentialClient = credentialClient;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserProfileDTO> register(@RequestBody UserRegisterDTO user) {
        UserProfileRegisterDTO registerDTO = new UserProfileRegisterDTO(user.getEmail(), user.getBio(), user.getIsPublic());
        ResponseEntity<UserProfileDTO> userProfileResponse = userCrudClient.register(registerDTO);
        UserProfileDTO userProfileDTO = userProfileResponse.getBody();
        log.info("UserProfileDTO with id: {}", userProfileDTO.id());
        credentialClient.register(new CredentialsDto(user.getUsername(), user.getPassword(), userProfileDTO.id()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileDTO);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {

        try {
            String currentUser = "anonymous";
            UserDTO dto = userCrudClient.getUserByUsername(username, currentUser);
            return ResponseEntity.ok(dto);
        } catch (FeignException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/deactivate/{username}")
    public ResponseEntity<String> deactivate(@PathVariable String username) {

        userCrudClient.deactivateUser(username);
        return ResponseEntity.ok("User deactivated");
    }

    @PutMapping("/update-profile/{username}")
    public ResponseEntity<String> updateUser(
            @PathVariable String username,
            @RequestBody UpdateUserDTO dto

    ) {
//
        return ResponseEntity.ok(userCrudClient.updateUser(dto, username));
    }

    @PostMapping("/recovery/{username}")
    public ResponseEntity<String> recover(@PathVariable String username) {

        userCrudClient.recoverUser(username);
        return ResponseEntity.ok("User recovered");
    }

    @PostMapping("/set-public/{username}")
    public ResponseEntity<String> setPublic(@PathVariable String username) {
        userCrudClient.setPublic(username);
        return ResponseEntity.ok("User set to public");
    }

    @PostMapping("/set-private/{username}")
    public ResponseEntity<String> setPrivate(@PathVariable String username) {
        userCrudClient.setPrivate(username);
        return ResponseEntity.ok("User set to private");
    }

    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        userCrudClient.deleteUser(username);
        return ResponseEntity.ok("User deleted");
    }
}