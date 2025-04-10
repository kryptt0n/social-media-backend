package com.socialmedia.mssspost.service;

import com.socialmedia.mssspost.dto.CreatePostRequestDto;
import com.socialmedia.mssspost.dto.PostResponseDto;
import com.socialmedia.mssspost.dto.UpdatePostRequestDto;
import com.socialmedia.mssspost.entity.Post;
import com.socialmedia.mssspost.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public PostResponseDto createPost(CreatePostRequestDto request) {
        Post post = Post.builder()
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .username(request.getUsername())
                .createdAt(LocalDateTime.now())
                .build();
        return toResponse(postRepository.save(post));
    }

    @Override
    public PostResponseDto updatePost(Integer postId, UpdatePostRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

//        post.setContent(request.getContent());
//        post.setImageUrl(request.getImageUrl());
        post.setContent(request.getContent());
        Post updated = postRepository.save(post);

        return toResponse(updated);
    }

    @Override
    public void deletePost(Integer id) {
        if (!postRepository.existsById(id)) {
//            throw new RuntimeException("Post not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        postRepository.deleteById(id);
    }
    @Override
    public List<PostResponseDto> getAllPosts() {
//        List<Post> posts = postRepository.findAll();
//        return posts.stream().map(this::toResponse).toList();

        return postRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PostResponseDto getPostById(Integer id) {
        return postRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<PostResponseDto> getPostsByUsername(String username) {
        return postRepository.findAllByUsername(username, Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PostResponseDto> getByFollowedUsernames(List<String> usernames) {
        List<Post> posts = postRepository.findAllByUsernameIn(usernames, Sort.by(Sort.Direction.DESC, "createdAt"));
        return posts.stream()
                .map(this::toResponse)
                .toList();
    }

    private PostResponseDto toResponse(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .username(post.getUsername())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
