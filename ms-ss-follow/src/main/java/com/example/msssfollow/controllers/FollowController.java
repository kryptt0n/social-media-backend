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

    @PostMapping("/create")
    public ResponseEntity<Void> follow(@RequestBody FollowRequestDto requestDto) {
        followService.follow(requestDto);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<Void> unfollow(@RequestParam String followerName, @RequestParam String followedName) {
        followService.unfollow(followerName, followedName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity<List<String>> getFollowers(@PathVariable String username) {
        return ResponseEntity.ok(followService.getFollowers(username));
    }

    @GetMapping("/followed/{username}")
    public ResponseEntity<List<String>> getFollowed(@PathVariable String username) {
        return ResponseEntity.ok(followService.getFollowed(username));
    }

    @GetMapping("/follow-data/{username}")
    public ResponseEntity<FollowResponseDto> getData(@PathVariable String username) {
        FollowResponseDto responseDto = new FollowResponseDto();
        responseDto.setFollowedCount(followService.countFollowed(username));
        responseDto.setFollowerCount(followService.countFollowers(username));

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/is-followed")
    public ResponseEntity<Boolean> getIsFollowed(@RequestParam String currentName, @RequestParam String username) {
        return ResponseEntity.ok(followService.isFollowed(currentName, username));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        followService.deleteAll(username);
        return ResponseEntity.noContent().build();
    }
}

