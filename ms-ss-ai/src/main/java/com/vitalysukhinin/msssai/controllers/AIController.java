package com.vitalysukhinin.msssai.controllers;

import com.vitalysukhinin.msssai.dtos.GeneratedPostContent;
import com.vitalysukhinin.msssai.services.AIService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@AllArgsConstructor
public class AIController {

    private AIService aiService;

    @GetMapping("/post")
    public ResponseEntity<GeneratedPostContent> generatePostContent() {
        return ResponseEntity.ok(aiService.generatePostContent());
    }
}
