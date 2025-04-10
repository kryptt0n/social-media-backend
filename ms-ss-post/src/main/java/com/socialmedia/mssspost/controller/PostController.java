package com.socialmedia.mssspost.controller;

import com.socialmedia.mssspost.dto.PostRequestDto;
import com.socialmedia.mssspost.dto.PostResponseDto;
import com.socialmedia.mssspost.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> create(@RequestBody @Valid PostRequestDto request) {
        return new ResponseEntity<>(postService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> get(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

