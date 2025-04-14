package com.socialmedia.mssspost.controller;

import com.socialmedia.mssspost.dto.CreatePostRequestDto;
import com.socialmedia.mssspost.dto.PostResponseDto;
import com.socialmedia.mssspost.dto.UpdatePostRequestDto;
import com.socialmedia.mssspost.service.PostServiceImpl;
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
    private final PostServiceImpl postService;


    // Get all posts created by a specific user
    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUsername(@PathVariable String username) {
        List<PostResponseDto> posts = postService.getPostsByUsername(username);
        return posts.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(posts);
    }

    // Get all posts
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllPosts();
        return posts.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(posts);
    }

    // Get posts from followed users
    // This will be handled through orchestration. Here, we just stub the endpoint.
    @GetMapping("/followed/{username}")
    public ResponseEntity<List<PostResponseDto>> getPostsFromFollowedUsers() {
        // request to users to get all followed users

        return ResponseEntity.notFound().build();
    }

    // Create a new post
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid CreatePostRequestDto request) {
        PostResponseDto created = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Update a post by ID
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Integer postId, @RequestBody UpdatePostRequestDto request) {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    // Delete a post by ID
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}

