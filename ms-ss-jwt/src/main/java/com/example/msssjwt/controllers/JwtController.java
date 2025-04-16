package com.example.msssjwt.controllers;

import com.example.msssjwt.dto.GenerateTokenDto;
import com.example.msssjwt.dto.JwtKeyDto;
import com.example.msssjwt.dto.TokenValidateDto;
import com.example.msssjwt.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
public class JwtController {

    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<JwtKeyDto> generateToken(@RequestBody GenerateTokenDto request) {
        JwtKeyDto jwtKeyDto = new JwtKeyDto(jwtService.generateToken(request.getUsername()));
        return ResponseEntity.ok(jwtKeyDto);
    }

    @PostMapping("/introspect")
    public ResponseEntity<JwtKeyDto> validateToken(@RequestBody TokenValidateDto validateDto) {
        if (jwtService.isTokenValid(validateDto.getToken()))
            return ResponseEntity.ok(new JwtKeyDto(validateDto.getToken()));
        else
            return ResponseEntity.badRequest().build();
    }



}
