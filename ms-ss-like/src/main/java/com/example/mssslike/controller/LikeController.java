package com.example.mssslike.controller;

import com.example.mssslike.dto.LikeRequestDto;
import com.example.mssslike.entity.Like;
import com.example.mssslike.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Like> likePost(@RequestBody LikeRequestDto requestDto) {
        return ResponseEntity.ok(likeService.createLike(requestDto));
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikePost(@RequestBody LikeRequestDto requestDto) {
        likeService.deleteLike(requestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Integer postId) {
        return ResponseEntity.ok(likeService.getLikeCountByPostId(postId));
    }
}
