package com.example.mainservice.services;

import com.example.mainservice.dto.CommentDTO;
import com.example.mainservice.entities.Comment;
import com.example.mainservice.entities.Post;
import com.example.mainservice.entities.User;
import com.example.mainservice.repositories.CommentRepository;
import com.example.mainservice.repositories.PostRepository;
import com.example.mainservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final DtoMapperService dtoMapperService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, DtoMapperService dtoMapperService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.dtoMapperService = dtoMapperService;
    }


    public List<CommentDTO> getCommentByPost(Integer postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            List<Comment> comments = commentRepository.findByPostOrderByCreatedAtDesc(post.get());
            return comments
                    .stream()
                    .map(dtoMapperService.commentToCommentDTO())
                    .toList();
        }
        return List.of();
    }

    public Comment create(Comment comment, String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            comment.setUser(userOptional.get());
            comment.setCreatedAt(LocalDateTime.now());
            return commentRepository.save(comment);
        }
        return null;
    }

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}
