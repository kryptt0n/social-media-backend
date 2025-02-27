package com.j2ee.socialmedia.controllers;

import com.j2ee.socialmedia.dto.UpdateUserDTO;
import com.j2ee.socialmedia.dto.UserDTO;
import com.j2ee.socialmedia.entities.User;
import com.j2ee.socialmedia.services.UserService;
import com.j2ee.socialmedia.services.JwtService;
import com.j2ee.socialmedia.services.storage.FileSystemStorageService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final FileSystemStorageService storageService;

    public UserController(UserService userService, JwtService jwtService, FileSystemStorageService storageService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.storageService = storageService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestPart("user") User user, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String newFilename = timestamp + "_" + originalFilename;
            storageService.store(file, newFilename);
            String fileUrl = storageService.generateFileUrl(newFilename);
            user.setImageUrl(fileUrl);
        }
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> authenticatedUser = userService.authenticateUser(user);
        if (authenticatedUser.isPresent()) {
            String token = jwtService.generateToken(authenticatedUser.get());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username, Authentication auth) {
        Optional<UserDTO> userOptional = userService.getUserByUsername(username, auth.getName());
        if (userOptional.isPresent())
            return ResponseEntity.ok(userOptional.get());

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/deactivate/{username}")
    public ResponseEntity<String> deactivateUser(@PathVariable String username, Authentication auth) {
        if (username.equals(auth.getName())) {
            userService.deactivateUser(username);
            return ResponseEntity.ok("User deactivated");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not match");
        }
    }

    @PostMapping("/recovery/{username}")
    public ResponseEntity<String> recoverUser(@PathVariable String username, Authentication auth) {
        if (username.equals(auth.getName())) {
            userService.recoverUser(username);
            return ResponseEntity.ok("User recovered");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not match");
        }
    }

    @PostMapping("/set-public")
    public ResponseEntity<String> setPublic(Authentication auth) {
        userService.setPublic(auth.getName());
        return ResponseEntity.ok("User set to public");
    }

    @PostMapping("/set-private")
    public ResponseEntity<String> setPrivate(Authentication auth) {
        userService.setPrivate(auth.getName());
        return ResponseEntity.ok("User set to private");
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(Authentication auth) {
        userService.deleteUser(auth.getName());
        return ResponseEntity.ok("User deleted");
    }

    @PatchMapping("/update-profile/{username}")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserDTO updateUserDTO, @PathVariable String username, Authentication auth) {
        if (!username.equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Integer> userIdOptional = userService.getIdByUsername(username);
        if (userIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<User> updatedUser = userService.updateUser(updateUserDTO, userIdOptional.get());
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
        }
    }
}
