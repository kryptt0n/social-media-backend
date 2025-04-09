package com.example.mainservice.services;

import com.example.mainservice.entities.Like;
import com.example.mainservice.entities.Post;
import com.example.mainservice.entities.User;
import com.example.mainservice.repositories.LikeRepository;
import com.example.mainservice.repositories.PostRepository;
import com.example.mainservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public LikeService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    public Like create(Like like, String username) {
        Optional<Post> postRepo = postRepository.findById(like.getPost().getId());
        Optional<User> userRepo = userRepository.findByUsername(username);
        if (postRepo.isPresent() && userRepo.isPresent()) {
            Post post = postRepo.get();
            User user = userRepo.get();
            like.setUser(user);
            like.setPost(post);
            like.setCreatedAt(LocalDateTime.now());
            return likeRepository.save(like);
        }
        return null;
    }

    public Integer getLikeCountByPost(Integer postId) {
        Optional<Post> postRepo = postRepository.findById(postId);
        if (postRepo.isPresent()) {
            Post post = postRepo.get();
            return post.getLikes().size();
        }
        return null;
    }

    public void deleteLike(Like like, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Post> post = postRepository.findById(like.getPost().getId());

        if (user.isPresent() && post.isPresent()) {
            Optional<Like> likeToDelete = likeRepository.findByUserAndPost(user.get(), post.get());
            likeToDelete.ifPresent(value -> likeRepository.deleteById(value.getId()));
        }
    }
}
