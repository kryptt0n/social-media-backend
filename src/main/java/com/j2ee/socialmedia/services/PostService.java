package com.j2ee.socialmedia.services;

import com.j2ee.socialmedia.dto.PostDTO;
import com.j2ee.socialmedia.dto.UserDTO;
import com.j2ee.socialmedia.entities.Post;
import com.j2ee.socialmedia.entities.User;
import com.j2ee.socialmedia.repositories.LikeRepository;
import com.j2ee.socialmedia.repositories.PostRepository;
import com.j2ee.socialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final LikeRepository likeRepository;
    private final DtoMapperService dtoMapperService;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository, DtoMapperService dtoMapperService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.dtoMapperService = dtoMapperService;
    }

    public PostDTO create(Post post, String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            post.setUser(user);
            post.setCreatedAt(LocalDateTime.now());
            post.setLikes(new HashSet<>());
            post.setComments(new HashSet<>());
            Post saved = postRepository.save(post);
            return dtoMapperService.postToPostDTO(user.getId()).apply(saved);
        } else {
            return null;
        }
    }

    public Optional<PostDTO> update(Integer postId, Post post) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            return Optional.empty();
        }

        post.setId(postId);
        Post saved = postRepository.save(post);
        return Optional.of(dtoMapperService.postToPostDTO(post.getUser().getId()).apply(saved));
    }

    public void deletePostById(int postId) {
        postRepository.deleteById(postId);
    }

    public List<PostDTO> getPostsByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Post> posts = postRepository.findAllByUser(user);
            List<PostDTO> result = posts
                    .stream()
                    .map(dtoMapperService.postToPostDTO(user.getId()))
                    .toList();
            return result;
        }
        return null;
    }
}
