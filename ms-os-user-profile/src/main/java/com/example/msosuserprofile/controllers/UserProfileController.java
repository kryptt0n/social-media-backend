package com.example.msosuserprofile.controllers;

import com.example.msosuserprofile.dto.UpdateUserDTO;
import com.example.msosuserprofile.dto.UserDTO;
import com.example.msosuserprofile.feign.UserCrudClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class UserProfileController {
    private static final Logger log =  LoggerFactory.getLogger(UserProfileController.class);

    private final UserCrudClient userCrudClient;

    public UserProfileController(UserCrudClient userCrudClient) {
        this.userCrudClient = userCrudClient;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserDTO> register(
            @RequestPart("user") UserDTO user,
            @RequestPart("password") String password) {

        return userCrudClient.register(user, password); // ✅ forward as JSON + param
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