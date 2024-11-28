package com.j2ee.socialmedia.services;

import com.j2ee.socialmedia.dto.UserDTO;
import com.j2ee.socialmedia.entities.Post;
import com.j2ee.socialmedia.entities.User;
import com.j2ee.socialmedia.repositories.PostRepository;
import com.j2ee.socialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public FollowService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post create(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> update(Integer postId, Post post) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()) {
            return Optional.empty();
        }

        post.setId(postId);
        return Optional.of(postRepository.save(post));
    }

    public Post getPostById(int postId) {
        return postRepository.getById(postId);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void deletePostById(int postId) {
        postRepository.deleteById(postId);
    }

    public List<Post> getPostsByUserId(int userId) {
        User user = userRepository.getById(userId);
        return postRepository.findAllByUser(user);
    }

    public void follow(String username, String name) {
    }

    public void unfollow(String username, String name) {
    }

    public List<UserDTO> getFollowers(String username, Authentication auth) {
        return null;
    }

    public List<UserDTO> getFollowed(String username, Authentication auth) {
        return null;
    }
}
