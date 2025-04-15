package com.socialmedia.msospost.client;

import com.socialmedia.msospost.dto.LikeRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-ss-like")
public interface LikeClient {

    @GetMapping("/like/count/{postId}")
    Integer getLikeCount(@PathVariable("postId") Integer postId);

    @PostMapping("/like")
    void likePost(@RequestBody LikeRequestDto requestDto);

    @DeleteMapping("/like")
    void unlikePost(@RequestBody LikeRequestDto requestDto);

    @DeleteMapping("/like/post/{postId}")
    void deleteAllByPostId(@PathVariable Integer postId);

}

