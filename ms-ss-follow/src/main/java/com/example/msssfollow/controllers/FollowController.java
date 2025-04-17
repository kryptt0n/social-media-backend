package com.example.msssfollow.controllers;


import com.example.msssfollow.dto.FollowRequestDto;
import com.example.msssfollow.dto.FollowResponseDto;
import com.example.msssfollow.entities.Follow;
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
    public ResponseEntity<Void> follow(@RequestBody FollowRequestDto requestDto) {
        followService.follow(requestDto);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unfollow(@RequestParam Integer userId, @RequestParam Integer currentUserId) {
        followService.unfollow(userId, currentUserId);
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

    @GetMapping("/follow-data")
    public ResponseEntity<FollowResponseDto> getData(@RequestParam Integer userId, @RequestParam Integer currentUserId) {
        FollowResponseDto responseDto = new FollowResponseDto();
        responseDto.setFollowed(followService.isFollowed(userId, currentUserId));
        responseDto.setFollowedCount(followService.countFollowed(userId));
        responseDto.setFollowerCount(followService.countFollowers(userId));

        return ResponseEntity.ok(responseDto);
    }
}

