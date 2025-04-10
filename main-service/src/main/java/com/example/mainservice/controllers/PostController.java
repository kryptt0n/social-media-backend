package com.example.mainservice.controllers;

import com.example.mainservice.dto.PostDTO;
import com.example.mainservice.entities.Post;
import com.example.mainservice.services.PostService;
import com.example.mainservice.services.storage.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("posts")
public class PostController {

    private final PostService postService;
    private final FileSystemStorageService storageService;

    @Autowired
    public PostController(PostService postService, FileSystemStorageService storageService) {
        this.postService = postService;
        this.storageService = storageService;
    }

    @GetMapping(path = "/user/{username}")
    public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable String username) {
        List<PostDTO> posts = postService.getPostsByUsername(username);

        if (posts.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(posts);
        }
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(Authentication auth) {
        List<PostDTO> posts = postService.getAllPosts(auth.getName());

        if (posts.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/followed")
    public ResponseEntity<List<PostDTO>> getFollowedPosts(Authentication auth) {
        System.out.println("in /followed");
        List<PostDTO> posts = postService.getFollowedPosts(auth.getName());

        if (posts.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestPart("post") Post post, Authentication auth, @RequestParam(value = "file", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String newFilename = timestamp + "_" + originalFilename;
            storageService.store(file, newFilename);
            String fileUrl = storageService.generateFileUrl(newFilename);
            post.setImageUrl(fileUrl);
        }

        PostDTO createdPost = postService.create(post, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping(path = "/{postId}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Integer postId, @RequestBody Post post) {

        Optional<PostDTO> updatedPost = postService.update(postId, post);

        if (updatedPost.isPresent()) {
            return ResponseEntity.ok(updatedPost.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{postId}")
    public void deletePost(@PathVariable Integer postId) {
        postService.deletePostById(postId);
    }

}
