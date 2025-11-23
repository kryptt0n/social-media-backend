package com.socialmedia.msospost.controller;

import com.socialmedia.msospost.client.MediaClient;
import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.client.UserClient;
import com.socialmedia.msospost.dto.*;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.PostWorkflowRunner;
import com.socialmedia.msospost.sequence.processor.LikePostProcessor;
import com.socialmedia.msospost.sequence.processor.FollowedPostsProcessor;
import com.socialmedia.msospost.sequence.processor.UnlikePostProcessor;
import com.socialmedia.msospost.sequence.processor.DeletePostProcessor;
import com.socialmedia.msospost.service.CommentOrchestratorService;
import lombok.RequiredArgsConstructor;
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
    private final FollowedPostsProcessor followedPostsProcessor;
    private final LikePostProcessor likePostProcessor;
    private final UnlikePostProcessor unlikePostProcessor;
    private final CommentOrchestratorService commentOrchestratorService;
    private final DeletePostProcessor deletePostProcessor;
    private final PostClient postClient;
    private final MediaClient mediaClient;
    private final UserClient userClient;

    @GetMapping("/search")
    public ResponseEntity<PostFeedResponseDto> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int limit) {
        PostResponseDto postResponse = postClient.searchPosts(keyword, cursor, limit);
        List<PostFeedItemDto> enriched = postResponse.getPosts().stream()
                .map(post -> {
                    PostWorkflowContext ctx = new PostWorkflowContext();
                    ctx.setPostId(post.getId());
                    ctx.setUsername(post.getUsername());
                    System.out.println("avatar : " + post.getUsername());
                    runner.runFetchFlow(ctx);
                    return ctx.getFinalDto();
                }).collect(Collectors.toList());


        PostFeedResponseDto response = PostFeedResponseDto
                .builder()
                .posts(enriched)
                .hasMore(postResponse.isHasMore())
                .cursor(postResponse.getCursor())
                .build();

        return ResponseEntity.ok(response);
    }

    //TODO Change Page to custom DTO with post list, cursor and has next boolean value for infinity scroll
    @GetMapping("/user/{username}")
    public ResponseEntity<PostFeedResponseDto> getPostsByUser(@PathVariable String username,
                                                              @RequestParam(required = false) String cursor,
                                                              @RequestParam(defaultValue = "10") int limit) {
        PostResponseDto postResponse = postClient.getPostsByUsername(username, cursor, limit);
        List<PostFeedItemDto> enriched = postResponse.getPosts().stream().map(post -> {
            PostWorkflowContext ctx = new PostWorkflowContext();
            ctx.setPostId(post.getId());
            ctx.setUsername(post.getUsername());
            runner.runFetchFlow(ctx);
            return ctx.getFinalDto();
        }).toList();
        PostFeedResponseDto response = PostFeedResponseDto
                .builder()
                .posts(enriched)
                .hasMore(postResponse.isHasMore())
                .cursor(postResponse.getCursor())
                .build();

        return ResponseEntity.ok(response);
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
    public ResponseEntity<PostFeedResponseDto> getFollowedPosts(@PathVariable String username,
                                                                @RequestParam(required = false) String cursor,
                                                                @RequestParam(defaultValue = "10") int limit) {
        PostResponseDto followedPosts = followedPostsProcessor.getFollowedPosts(username, cursor, limit);
        List<PostFeedItemDto> enriched = followedPosts.getPosts().stream().map(post -> {
            PostWorkflowContext ctx = new PostWorkflowContext();
            ctx.setPostId(post.getId());
            ctx.setUsername(post.getUsername());
            runner.runFetchFlow(ctx);
            return ctx.getFinalDto();
        }).toList();

        PostFeedResponseDto response = PostFeedResponseDto
                .builder()
                .posts(enriched)
                .hasMore(followedPosts.isHasMore())
                .cursor(followedPosts.getCursor())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reported")
    public ResponseEntity<PostFeedResponseDto> getReportedPosts() {

        PostResponseDto postResponse = postClient.getReportedPosts();
        List<PostFeedItemDto> enriched = postResponse.getPosts().stream().map(post -> {
            PostWorkflowContext ctx = new PostWorkflowContext();
            ctx.setPostId(post.getId());
            runner.runFetchFlow(ctx);
            return ctx.getFinalDto();
        }).toList();

        PostFeedResponseDto response = PostFeedResponseDto
                .builder()
                .posts(enriched)
                .hasMore(postResponse.isHasMore())
                .cursor(postResponse.getCursor())
                .build();

        return ResponseEntity.ok(response);
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
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostRequestDto requestDto) {
        System.out.println("▶ [CreatePostController] Starting with username: " + requestDto.getUsername());

        PostWorkflowContext context = new PostWorkflowContext();
        context.setUsername(requestDto.getUsername());

        // ✅ Add base64Image to context for Kafka processing
        context.setBase64Image(requestDto.getBase64Image());

        PostDto post = new PostDto();
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
        List<CommentResponseDto> commentList = commentOrchestratorService.getCommentsByPost(postId);

        commentList.forEach(comment -> {
            String userId = userClient.getUserByUsername(comment.getUsername()).userId().toString();
            mediaClient.findBySourceIdAndProvider(userId, "PROFILE")
                    .ifPresentOrElse(
                            media -> {
                                String imageUrl = media.getUrl();
                                comment.setAvatarUrl(imageUrl);
                            },
                            () -> System.out.println("No media found for username: " + userId)
                    );
        });

        return ResponseEntity.ok(commentList);
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

    @PostMapping("/report/{postId}")
    public ResponseEntity<Void> reportPost(@PathVariable Integer postId) {
         postClient.reportPost(postId);
        return ResponseEntity.noContent().build();

    }

}
