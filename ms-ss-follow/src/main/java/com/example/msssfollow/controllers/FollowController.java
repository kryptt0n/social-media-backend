package com.example.msssfollow.controllers;


import com.example.msssfollow.services.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    public ResponseEntity<Void> follow(@RequestParam Integer followerId, @RequestParam Integer followedId) {
        followService.follow(followerId, followedId);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unfollow(@RequestParam Integer followerId, @RequestParam Integer followedId) {
        followService.unfollow(followerId, followedId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<Integer>> getFollowers(@PathVariable Integer userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    @GetMapping("/followed/{userId}")
    public ResponseEntity<List<Integer>> getFollowed(@PathVariable Integer userId) {
        return ResponseEntity.ok(followService.getFollowed(userId));
    }
}

