package com.j2ee.socialmedia.controllers;

import com.j2ee.socialmedia.dto.UserDTO;
import com.j2ee.socialmedia.entities.User;
import com.j2ee.socialmedia.services.UserService;
import com.j2ee.socialmedia.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody User user) {
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
}
