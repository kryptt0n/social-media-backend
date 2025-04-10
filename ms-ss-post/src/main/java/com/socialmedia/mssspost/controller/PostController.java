package com.socialmedia.mssspost.controller;

import com.socialmedia.mssspost.dto.PostRequestDto;
import com.socialmedia.mssspost.dto.PostResponseDto;
import com.socialmedia.mssspost.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> create(@RequestBody @Valid PostRequestDto request) {
        PostResponseDto created = postService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> get(@PathVariable Integer postId) {
        return ResponseEntity.ok(postService.getById(postId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUser(@PathVariable Integer userId) {
        List<PostResponseDto> posts = postService.getByUserId(userId);
        return posts.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(posts);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Integer postId, @RequestBody PostRequestDto request) {
        return ResponseEntity.ok(postService.update(postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable Integer postId) {
        postService.deleteById(postId);
        return ResponseEntity.noContent().build();
    }
}

