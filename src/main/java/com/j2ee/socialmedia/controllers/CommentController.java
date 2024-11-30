package com.j2ee.socialmedia.controllers;

import com.j2ee.socialmedia.entities.Comment;
import com.j2ee.socialmedia.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsForPost(@RequestParam int postId) {
        List<Comment> comments = commentService.getCommentByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment, Authentication auth) {
        commentService.create(comment, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
