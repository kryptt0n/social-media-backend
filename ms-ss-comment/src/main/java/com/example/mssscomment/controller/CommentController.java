package com.example.mssscomment.controller;

import com.example.mssscomment.dto.CommentRequestDto;
import com.example.mssscomment.entity.Comment;
import com.example.mssscomment.repository.CommentRepository;
import com.example.mssscomment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.createComment(requestDto));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Integer postId) {
        return ResponseEntity.ok(commentService.getCommentByPost(postId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
