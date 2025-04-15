package com.socialmedia.msospost.client;

import com.socialmedia.msospost.dto.CreatePostRequestDto;
import com.socialmedia.msospost.dto.PostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-ss-post")
public interface PostClient {

    @PostMapping("/posts")
    PostResponseDto createPost(@RequestBody CreatePostRequestDto request);

    @GetMapping("/posts/{id}")
    PostResponseDto getPostById(@PathVariable("id") Integer id);

    @DeleteMapping("/posts/{postId}")
    void deletePost(@PathVariable Integer postId);
}

