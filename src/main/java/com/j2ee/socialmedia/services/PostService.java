package com.j2ee.socialmedia.services;

import com.j2ee.socialmedia.dto.PostDTO;
import com.j2ee.socialmedia.entities.Post;
import com.j2ee.socialmedia.entities.User;
import com.j2ee.socialmedia.repositories.LikeRepository;
import com.j2ee.socialmedia.repositories.PostRepository;
import com.j2ee.socialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final LikeRepository likeRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    public Post create(Post post, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            post.setUser(user.get());
            post.setCreatedAt(LocalDateTime.now());
            return postRepository.save(post);
        } else {
            return null;
        }
    }

    public Optional<Post> update(Integer postId, Post post) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            return Optional.empty();
        }

        post.setId(postId);
        return Optional.of(postRepository.save(post));
    }

    public void deletePostById(int postId) {
        postRepository.deleteById(postId);
    }

    public List<PostDTO> getPostsByUserId(int userId) {
        User user = userRepository.getById(userId);
        List<Post> posts = postRepository.findAllByUser(user);
        List<PostDTO> result = posts.stream().map(post ->
            new PostDTO(
                    post.getId(),
                    post.getContent(),
                    post.getImage(),
                    post.getUser(),
                    post.getCreatedAt(),
                    post.getLikes().stream().anyMatch(like -> like.getUser().getId().equals(userId)),
                    post.getLikes().size()
            )
        ).toList();
        return result;
    }
}
