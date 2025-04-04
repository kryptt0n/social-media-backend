package com.example.mainservice.services;

import com.example.mainservice.dto.PostDTO;
import com.example.mainservice.entities.Follow;
import com.example.mainservice.entities.Post;
import com.example.mainservice.entities.User;
import com.example.mainservice.repositories.FollowRepository;
import com.example.mainservice.repositories.PostRepository;
import com.example.mainservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    private final DtoMapperService dtoMapperService;
    private final FollowRepository followRepository;
    private final PostComparator postComparator;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, DtoMapperService dtoMapperService, FollowRepository followRepository, PostComparator postComparator) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.dtoMapperService = dtoMapperService;
        this.followRepository = followRepository;
        this.postComparator = postComparator;
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
            return dtoMapperService.postToPostDTO(username).apply(saved);
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
        return Optional.of(dtoMapperService.postToPostDTO(post.getUser().getUsername()).apply(saved));
    }

    public void deletePostById(int postId) {
        postRepository.deleteById(postId);
    }

    public List<PostDTO> getPostsByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Post> posts = postRepository.findAllByUserOrderByCreatedAtDesc(user);
            List<PostDTO> result = posts
                    .stream()
                    .map(dtoMapperService.postToPostDTO(username))
                    .toList();
            return result;
        }
        return List.of();
    }

    public List<PostDTO> getAllPosts(String username) {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Order.desc("createdAt")));
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return posts.stream().map(dtoMapperService.postToPostDTO(username)).toList();
        }
        return List.of();
    }

    public List<PostDTO> getFollowedPosts(String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Follow> follows = followRepository.findAllByFollower(user);
            List<Post> posts = new ArrayList<>(follows.stream()
                    .map(follow -> follow.getFollowed().getPosts().stream()
                            .toList())
                    .toList()
                    .stream()
                    .flatMap(List::stream).toList());
            posts.sort(postComparator);
            return posts.stream().map(dtoMapperService.postToPostDTO(username)).toList();
        }

        return List.of();
    }
}
