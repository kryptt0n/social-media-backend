package com.example.msssuserprofilecrud.controllers;

import com.example.msssuserprofilecrud.dto.UpdateUserDTO;
import com.example.msssuserprofilecrud.dto.UserProfileDTO;
import com.example.msssuserprofilecrud.dto.UserEmailDTO;
import com.example.msssuserprofilecrud.dto.UserStatsResponse;
import com.example.msssuserprofilecrud.dto.UserProfileRegisterDTO;
import com.example.msssuserprofilecrud.entities.User;
import com.example.msssuserprofilecrud.services.UserCrudService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ConfigurationPropertiesScan("com.example.msssuserprofilecrud.configs")

@RestController
@RequestMapping("/usercrud")
public class UserCrudController {
    private static final Logger log = LoggerFactory.getLogger(UserCrudController.class);

    private final UserCrudService userService;

    public UserCrudController(UserCrudService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserProfileDTO> register(@RequestBody UserProfileRegisterDTO userDto) {
//        log.info("Received registration request for user: {}", userDto.username());

        User user = new User();
        user.setBio(userDto.getBio());
        user.setAccountNonLocked(true);
        user.setPublic(user.isPublic());
        user.setEmail(userDto.getEmail());

        User savedUser = userService.registerUser(user);

        UserProfileDTO result = new UserProfileDTO(savedUser.getId(), savedUser.getBio(), savedUser.getEmail(), savedUser.isAccountNonLocked(), savedUser.isPublic());
        log.info("User saved successfully: {}", result.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<UserProfileDTO> getUser(@PathVariable Integer userId) {
        log.info("Fetching user by username: {}", userId);

        return userService.getUserByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("User not found: {}", userId);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/emails/{email}")
    public ResponseEntity<UserEmailDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping("/deactivate/{userId}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Integer userId) {
        userService.deactivateUser(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserDTO dto, @PathVariable Integer userId) {
        return userService.updateUser(dto, userId)
                .map(user -> ResponseEntity.ok("User updated"))
                .orElse(ResponseEntity.internalServerError().body("Update failed"));
    }

    @PostMapping("/recover/{userId}")
    public ResponseEntity<Void> recoverUser(@PathVariable Integer userId) {
        userService.recoverUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/set-public/{userId}")
    public ResponseEntity<Void> setPublic(@PathVariable Integer userId) {
        userService.setPublic(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/set-private/{userId}")
    public ResponseEntity<Void> setPrivate(@PathVariable Integer userId) {
        userService.setPrivate(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserProfileDTO>> getAllUsers() {
        List<UserProfileDTO> users = userService.getAllUserProfiles();  // ⬅️ Call service
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/stats")
    public UserStatsResponse getUserStats() {
        return userService.getUserStats();
    }

}

