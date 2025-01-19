package com.j2ee.socialmedia.controllers;

import com.j2ee.socialmedia.dto.UserDTO;
import com.j2ee.socialmedia.services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{username}")
    public ResponseEntity<Void> followUser(@PathVariable String username, Authentication auth) {
        followService.follow(username, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> unfollowUser(@PathVariable String username, Authentication auth) {
        followService.unfollow(username, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity<List<UserDTO>> getFollowers(@PathVariable String username, Authentication auth) {
        List<UserDTO> followers = followService.getFollowers(username, auth.getName());
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/followed/{username}")
    public ResponseEntity<List<UserDTO>> getFollowed(@PathVariable String username, Authentication auth) {
        List<UserDTO> followers = followService.getFollowed(username, auth.getName());
        return ResponseEntity.ok(followers);
    }

}
