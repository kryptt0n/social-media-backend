package com.example.mssspostcrud.controller;

import com.example.mssspostcrud.dto.FollowingPostRequestDto;
import com.example.mssspostcrud.entity.Post;
import com.example.mssspostcrud.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/search")
    public Page<Post> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postService.searchPosts(keyword, pageable);
    }

    @GetMapping("/{username}")
    public Page<Post> userPosts(@PathVariable String username,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postService.getPostsByUsername(username, pageable);
    }

    @GetMapping("/followed")
    public Page<Post> getFollowedPosts(@RequestBody FollowingPostRequestDto requestDto,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postService.getFollowedPosts(requestDto.getUsernames(), pageable);
    }
}
