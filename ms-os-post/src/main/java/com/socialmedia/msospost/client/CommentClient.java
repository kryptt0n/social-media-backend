package com.socialmedia.msospost.client;

import com.socialmedia.msospost.dto.CommentRequestDto;
import com.socialmedia.msospost.dto.CommentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-ss-comment")
public interface CommentClient {

    @GetMapping("/comment/post/{postId}")
    List<CommentResponseDto> getCommentsByPost(@PathVariable Integer postId);

    @PostMapping("/comment")
    CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto);

    @DeleteMapping("/comment/post/{postId}")
    void deleteAllByPostId(@PathVariable Integer postId);

    @DeleteMapping("/comment/{commentId}")
    void deleteCommentById(@PathVariable Integer commentId);
}

