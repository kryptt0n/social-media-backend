package com.example.msosadmin.controllers;

import com.example.msosadmin.dto.PostFeedItemDto;
import com.example.msosadmin.dto.StatsResponseDto;
import com.example.msosadmin.dto.UserProfileDTO;
import com.example.msosadmin.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserProfileDTO>> getAllUsers() {
        System.out.println("🔹 AdminController: getAllUsers() API called");
        List<UserProfileDTO> users = adminService.getAllUsers();
        System.out.println("🔹 Response size: " + users.size());

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/posts/reported")
    public ResponseEntity<List<PostFeedItemDto>> getReportedPosts() {
        List<PostFeedItemDto> posts = adminService.getReportedPosts();
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponseDto> getStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @PostMapping("/deactivate/{userId}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Integer userId) {
        adminService.deactivateUser(userId);
        return ResponseEntity.ok().build();
    }
}
