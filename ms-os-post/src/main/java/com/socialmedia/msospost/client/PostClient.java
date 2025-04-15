package com.socialmedia.msospost.client;

import com.socialmedia.msospost.dto.CreatePostRequestDto;
import com.socialmedia.msospost.dto.PostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-ss-post")
public interface PostClient {

    @PostMapping("/posts")
    PostResponseDto createPost(@RequestBody CreatePostRequestDto request);

    @GetMapping("/posts/{id}")
    PostResponseDto getPostById(@PathVariable("id") Integer id);
}

