package com.example.msssmediaexchange.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthTestController {

    @GetMapping("/test")
    public String test() {
        return "✅ ms-ss-media-exchange is working!";
    }
}
