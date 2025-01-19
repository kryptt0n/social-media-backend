package com.j2ee.socialmedia.controllers;

import com.j2ee.socialmedia.entities.Like;
import com.j2ee.socialmedia.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Integer postId) {
        Integer likeCount = likeService.getLikeCountByPost(postId);
        return ResponseEntity.ok(likeCount);
    }

    @PostMapping
    public ResponseEntity<Void> createLike(@RequestBody Like like, Authentication auth) {
        likeService.create(like, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLike(@RequestBody Like like, Authentication auth) {
        likeService.deleteLike(like, auth.getName());
        return ResponseEntity.noContent().build();
    }

}