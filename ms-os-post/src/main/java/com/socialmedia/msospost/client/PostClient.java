package com.socialmedia.msospost.client;

import com.socialmedia.msospost.dto.CreatePostRequestDto;
import com.socialmedia.msospost.dto.PostResponseDto;
import com.socialmedia.msospost.dto.StatsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-ss-post", path = "/posts")
public interface PostClient {

    // 1. GET /posts/search
    @GetMapping("/search")
    Page<PostResponseDto> searchPosts(@RequestParam(required = false) String keyword,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size);

    // 2. GET /posts/user/{username}
    @GetMapping("/user/{username}")
    Page<PostResponseDto> getPostsByUsername(@PathVariable String username,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size);
    // 4. GET /posts
    @GetMapping
    List<PostResponseDto> getAllPosts();

    // 5. POST /posts/followed/
    @PostMapping("/followed")
    Page<PostResponseDto> getFollowedPosts(@RequestBody List<String> usernames,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size);

    // 6. GET /posts/reported
    @GetMapping("/reported")
    List<PostResponseDto> getReportedPosts();

    // 7. GET /posts/stats
    @GetMapping("/stats")
    StatsResponseDto getPostStats();

    @PostMapping
    PostResponseDto createPost(@RequestBody CreatePostRequestDto request);

    @GetMapping("/{id}")
    PostResponseDto getPostById(@PathVariable("id") Integer id);

    @DeleteMapping("/{postId}")
    void deletePost(@PathVariable Integer postId);

    @PostMapping("/report/{postId}")
    void reportPost(@PathVariable("postId") Integer postId);
}

