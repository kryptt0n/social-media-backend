package com.j2ee.socialmedia.services;

import com.j2ee.socialmedia.dto.CommentDTO;
import com.j2ee.socialmedia.entities.Comment;
import com.j2ee.socialmedia.entities.Post;
import com.j2ee.socialmedia.entities.User;
import com.j2ee.socialmedia.repositories.CommentRepository;
import com.j2ee.socialmedia.repositories.PostRepository;
import com.j2ee.socialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final DtoMapperService dtoMapperService;
    private PostRepository postRepository;
    private UserRepository userRepository;

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
            List<Comment> comments = post.get().getComments().stream().toList();
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
