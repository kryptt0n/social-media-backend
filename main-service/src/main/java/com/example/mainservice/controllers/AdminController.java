package com.example.mainservice.controllers;


import com.example.mainservice.dto.DashboardStatsDTO;
import com.example.mainservice.dto.PostDTO;
import com.example.mainservice.dto.UserDTO;
import com.example.mainservice.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
        System.out.println("✅ AdminService has been injected into AdminController!");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        System.out.println("🔹 getAllUsers() API called"); // Debugging line
        List<UserDTO> users = adminService.getAllUsers();
        System.out.println("🔹 Response size: " + users.size()); // Debugging line

        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }


    @GetMapping("/stats")
    public DashboardStatsDTO getDashboardStats() {
        return adminService.getDashboardStats();
    }

    @GetMapping("/posts/reported")
    public ResponseEntity <List<PostDTO>>getReportedPosts() {
        List<PostDTO> posts = adminService.getReportedPosts();
        if (posts.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(posts);
        }
    }
}
