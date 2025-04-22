package com.socialmedia.mssspost.controller;

import com.socialmedia.mssspost.dto.CreatePostRequestDto;
import com.socialmedia.mssspost.dto.PostResponseDto;
import com.socialmedia.mssspost.dto.StatsResponseDto;
import com.socialmedia.mssspost.dto.UpdatePostRequestDto;
import com.socialmedia.mssspost.service.PostServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostServiceImpl postService;


    @GetMapping("/search")
    public ResponseEntity<Page<PostResponseDto>> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(postService.searchPosts(keyword, pageable));
    }

    // Get all posts created by a specific user
    @GetMapping("/user/{username}")
    public ResponseEntity<Page<PostResponseDto>> getPostsByUsername(@PathVariable String username,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PostResponseDto> posts = postService.getPostsByUsername(username, pageable);
        return posts.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getById(@PathVariable Integer postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    // Get all posts
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllPosts();
        return posts.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(posts);
    }

    // Get posts from followed users
    @PostMapping("/followed")
    public ResponseEntity<Page<PostResponseDto>> getPostsFromFollowedUsernames(
            @RequestBody List<String> usernames,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PostResponseDto> posts = postService.getByFollowedUsernames(usernames, pageable);
        return ResponseEntity.ok(posts);
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

    @GetMapping("/reported")
    public ResponseEntity<List<PostResponseDto>> getReportedPosts() {
        return ResponseEntity.ok(postService.getReportedPosts());
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponseDto> getStats() {
        return ResponseEntity.ok(postService.getStats());
    }

    @PostMapping("/report/{postId}")
    public ResponseEntity<Void> reportPost(@PathVariable Integer postId) {
        postService.reportPost(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> deletePost(@PathVariable String username) {
        postService.deleteAllByUsername(username);
        return ResponseEntity.noContent().build();
    }
}

