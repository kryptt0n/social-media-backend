package com.socialmedia.msospost.controller;

import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.*;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.PostWorkflowRunner;
import com.socialmedia.msospost.sequence.processor.LikePostProcessor;
import com.socialmedia.msospost.sequence.processor.UnlikePostProcessor;
import com.socialmedia.msospost.sequence.processor.DeletePostProcessor;
import com.socialmedia.msospost.service.CommentOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostOrchestrationController {

    private final PostWorkflowRunner runner;
    private final LikePostProcessor likePostProcessor;
    private final UnlikePostProcessor unlikePostProcessor;
    private final CommentOrchestratorService commentOrchestratorService;
    private final DeletePostProcessor deletePostProcessor;
    private final PostClient postClient;

    @GetMapping("/search")
    public ResponseEntity<Page<PostFeedItemDto>> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PostResponseDto> postPage = postClient.searchPosts(keyword, page, size);
        List<PostFeedItemDto> enriched = postPage.getContent().stream()
                .map(post -> {
                    PostWorkflowContext ctx = new PostWorkflowContext();
                    ctx.setPostId(post.getId());
                    runner.runFetchFlow(ctx);
                    return ctx.getFinalDto();
                }).collect(Collectors.toList());

        Page<PostFeedItemDto> enrichedPage = new PageImpl<>(enriched, postPage.getPageable(), postPage.getTotalElements());
        return ResponseEntity.ok(enrichedPage);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Page<PostFeedItemDto>> getPostsByUser(@PathVariable String username,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        Page<PostResponseDto> postPage = postClient.getPostsByUsername(username, page, size);
        List<PostFeedItemDto> enriched = postPage.getContent().stream().map(post -> {
            PostWorkflowContext ctx = new PostWorkflowContext();
            ctx.setPostId(post.getId());
            runner.runFetchFlow(ctx);
            return ctx.getFinalDto();
        }).toList();
        Page<PostFeedItemDto> enrichedPage = new PageImpl<>(enriched, postPage.getPageable(), postPage.getTotalElements());
        return ResponseEntity.ok(enrichedPage);
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<PostFeedItemDto>> getAllPosts() {
//        List<PostFeedItemDto> enriched = postClient.getAllPosts().stream().map(post -> {
//            PostWorkflowContext ctx = new PostWorkflowContext();
//            ctx.setPostId(post.getId());
//            runner.runFetchFlow(ctx);
//            return ctx.getFinalDto();
//        }).toList();
//        return ResponseEntity.ok(enriched);
//    }

    @GetMapping("/followed/{username}")
    public ResponseEntity<List<PostFeedItemDto>> getFollowedPosts(@PathVariable String username) {
        List<PostFeedItemDto> enriched = postClient.getFollowedPosts(username).stream().map(post -> {
            PostWorkflowContext ctx = new PostWorkflowContext();
            ctx.setPostId(post.getId());
            runner.runFetchFlow(ctx);
            return ctx.getFinalDto();
        }).toList();
        return ResponseEntity.ok(enriched);
    }

    @GetMapping("/reported")
    public ResponseEntity<List<PostFeedItemDto>> getReportedPosts() {
        List<PostFeedItemDto> enriched = postClient.getReportedPosts().stream().map(post -> {
            PostWorkflowContext ctx = new PostWorkflowContext();
            ctx.setPostId(post.getId());
            runner.runFetchFlow(ctx);
            return ctx.getFinalDto();
        }).toList();
        return ResponseEntity.ok(enriched);
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponseDto> getStats() {
        return ResponseEntity.ok(postClient.getPostStats());
    }

    @GetMapping("/feed/{postId}")
    public ResponseEntity<PostFeedItemDto> getFeed(@PathVariable Integer postId) {
        PostWorkflowContext context = new PostWorkflowContext();
        context.setPostId(postId);
        runner.runFetchFlow(context);
        System.out.println("📤 Returning post: " + context.getPost());
        System.out.println("📤 Returning finalDto: " + context.getFinalDto());

        return ResponseEntity.ok(context.getFinalDto());
    }

    @PostMapping("/create")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody CreatePostRequestDto requestDto) {
        System.out.println("▶ [CreatePostController] Starting with username: " + requestDto.getUsername());

        PostWorkflowContext context = new PostWorkflowContext();
        context.setUsername(requestDto.getUsername());

        // ✅ Add base64Image to context for Kafka processing
        context.setBase64Image(requestDto.getBase64Image());

        PostResponseDto post = new PostResponseDto();
        post.setContent(requestDto.getContent());
        context.setPost(post);

        runner.runCreateFlow(context);
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

    @GetMapping("/comment/post/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable Integer postId) {
        return ResponseEntity.ok(commentOrchestratorService.getCommentsByPost(postId));
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto requestDto) {
        System.out.println("📝 Creating comment: " + requestDto);
        return ResponseEntity.ok(commentOrchestratorService.createComment(requestDto));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        commentOrchestratorService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId) {
        PostWorkflowContext context = new PostWorkflowContext();
        context.setPostId(postId);

        deletePostProcessor.process(context);

        return ResponseEntity.noContent().build();
    }

}
