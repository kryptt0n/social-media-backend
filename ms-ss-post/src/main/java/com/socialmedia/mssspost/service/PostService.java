package com.socialmedia.mssspost.service;

import com.socialmedia.mssspost.dto.PostRequestDto;
import com.socialmedia.mssspost.dto.PostResponseDto;
import com.socialmedia.mssspost.entity.Post;
import com.socialmedia.mssspost.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostResponseDto create(PostRequestDto request) {
        Post post = Post.builder()
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .userId(request.getUserId())
                .createdAt(LocalDateTime.now())
                .build();
        return toResponse(postRepository.save(post));
    }

    public PostResponseDto getById(Integer id) {
        return postRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<PostResponseDto> getByUserId(Integer userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream().map(this::toResponse).toList();
    }

    public PostResponseDto update(Integer postId, PostRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());

        Post updated = postRepository.save(post);
        return toResponse(updated);
    }

    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }

    private PostResponseDto toResponse(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .userId(post.getUserId())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
