package com.socialmedia.msssidentity.controller;

import com.socialmedia.msssidentity.dto.JwtResponse;
import com.socialmedia.msssidentity.dto.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("🟢 Received login request for: " + loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // ✅ Generate secure key — must be at least 256 bits for HS256
        SecretKey key = Keys.hmacShaKeyFor("supersecretkeythatislongenough123!".getBytes());

        User user = (User) authentication.getPrincipal();

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 hour
                .signWith(key, SignatureAlgorithm.HS256) // ✅ Modern signing
                .compact();

        System.out.println("✅ JWT generated for user: " + user.getUsername());

        return ResponseEntity.ok(new JwtResponse(token));
    }
}
