package com.example.mssspostcrud.controller;

import com.example.mssspostcrud.dto.FollowingPostRequestDto;
import com.example.mssspostcrud.dto.StatsResponseDto;
import com.example.mssspostcrud.entity.Post;
import com.example.mssspostcrud.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/search")
    public ResponseEntity<Page<Post>> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(postService.searchPosts(keyword, pageable));
    }

    @GetMapping("/{username}")
    public ResponseEntity<Page<Post>> userPosts(@PathVariable String username,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(postService.getPostsByUsername(username, pageable));
    }

    @GetMapping("/followed")
    public ResponseEntity<Page<Post>> getFollowedPosts(@RequestBody FollowingPostRequestDto requestDto,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(postService.getFollowedPosts(requestDto.getUsernames(), pageable));
    }

    @GetMapping("/reported")
    public ResponseEntity<List<Post>> getReportedPosts() {
        return ResponseEntity.ok(postService.getReportedPosts());
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponseDto> getStats() {
        return ResponseEntity.ok(postService.getStats());
    }
}
