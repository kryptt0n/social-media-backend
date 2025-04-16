package com.example.msssuserprofilecrud.controllers;

import com.example.msssuserprofilecrud.dto.UpdateUserDTO;
import com.example.msssuserprofilecrud.dto.UserDTO;
import com.example.msssuserprofilecrud.entities.User;
import com.example.msssuserprofilecrud.services.UserCrudService;
import com.example.msssuserprofilecrud.repositories.UserRepository;

import com.example.msssuserprofilecrud.services.storage.FileSystemStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@ConfigurationPropertiesScan("com.example.msssuserprofilecrud.configs")

@RestController
@RequestMapping("/usercrud")
public class UserCrudController {
    private static final Logger log =  LoggerFactory.getLogger(UserCrudController.class);

    private final UserCrudService userService;
    private final FileSystemStorageService storageService;
    public UserCrudController(UserCrudService userService, FileSystemStorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(
            @RequestBody UserDTO userDto,
            @RequestParam String password) {
        log.info("Received registration request for user: {}", userDto.username());

        User user = new User();
        user.setUsername(userDto.username());
        user.setBio(userDto.bio());
        user.setImageUrl(userDto.imageUrl());
        user.setPassword(password);
        user.setAccountNonLocked(userDto.isActive());
        user.setPublic(userDto.isPublic());

        userService.registerUser(user);
        log.info("User saved successfully: {}", user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }





    @GetMapping("/users/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username, @RequestParam String currentUsername) {
        log.info("Fetching user by username: {}", username);

        return userService.getUserByUsername(username, currentUsername)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("User not found: {}", username);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping("/deactivate/{username}")
    public ResponseEntity<Void> deactivateUser(@PathVariable String username) {
        userService.deactivateUser(username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserDTO dto, @PathVariable String username) {
        return userService.getIdByUsername(username)
                .flatMap(id -> userService.updateUser(dto, id))
                .map(user -> ResponseEntity.ok("User updated"))
                .orElse(ResponseEntity.internalServerError().body("Update failed"));
    }

    @PostMapping("/recover/{username}")
    public ResponseEntity<Void> recoverUser(@PathVariable String username) {
        userService.recoverUser(username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/set-public/{username}")
    public ResponseEntity<Void> setPublic(@PathVariable String username) {
        userService.setPublic(username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/set-private/{username}")
    public ResponseEntity<Void> setPrivate(@PathVariable String username) {
        userService.setPrivate(username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}

