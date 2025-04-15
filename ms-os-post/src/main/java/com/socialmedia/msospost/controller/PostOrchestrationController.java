package com.socialmedia.msospost.controller;

import com.socialmedia.msospost.dto.*;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.PostWorkflowRunner;
import com.socialmedia.msospost.sequence.processor.LikePostProcessor;
import com.socialmedia.msospost.sequence.processor.UnlikePostProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orchestration")
public class PostOrchestrationController {

    private final PostWorkflowRunner runner;
    private final LikePostProcessor likePostProcessor;
    private final UnlikePostProcessor unlikePostProcessor;

    @GetMapping("/post-feed/{postId}")
    public ResponseEntity<PostFeedItemDto> getFeed(@PathVariable Integer postId) {
        PostWorkflowContext context = new PostWorkflowContext();
        context.setPostId(postId);
        runner.runFetchFlow(context);
        System.out.println("📤 Returning post: " + context.getPost());
        System.out.println("📤 Returning finalDto: " + context.getFinalDto());

        return ResponseEntity.ok(context.getFinalDto());
    }

    @PostMapping("/post-create")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody CreatePostRequestDto requestDto) {
        System.out.println("▶ [CreatePostController] Starting with username: " + requestDto.getUsername());

        PostWorkflowContext context = new PostWorkflowContext();

        context.setUsername(requestDto.getUsername());

        // Prepopulate partial post object to pass to the context
        PostResponseDto post = new PostResponseDto();
        post.setContent(requestDto.getContent());
        post.setImageUrl(requestDto.getImageUrl());

        context.setPost(post);

        runner.runCreateFlow(context); // Will use CreatePostProcessor (and optionally FetchPostById)

        // ✅ Debug: Confirm the postId is now set
        System.out.println("✅ [CreatePostController] Post created with ID: " + context.getPost().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(context.getPost());
    }

    @PostMapping("/like")
    public ResponseEntity<Void> likePost(@RequestBody LikeRequestDto requestDto) {
        PostWorkflowContext context = new PostWorkflowContext();
        context.setUsername(requestDto.getUsername());
        context.setPostId(requestDto.getPostId());

        likePostProcessor.process(context); // If not using runner, call directly

        return ResponseEntity.ok().build();
    }

    @PostMapping("/unlike")
    public ResponseEntity<Void> unlikePost(@RequestBody LikeRequestDto requestDto) {
        PostWorkflowContext context = new PostWorkflowContext();
        context.setUsername(requestDto.getUsername());
        context.setPostId(requestDto.getPostId());

        unlikePostProcessor.process(context);

        return ResponseEntity.ok().build();
    }
}
