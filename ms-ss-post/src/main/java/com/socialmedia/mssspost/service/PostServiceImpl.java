package com.socialmedia.mssspost.service;

import com.socialmedia.mssspost.dto.CreatePostRequestDto;
import com.socialmedia.mssspost.dto.PostResponseDto;
import com.socialmedia.mssspost.dto.StatsResponseDto;
import com.socialmedia.mssspost.dto.UpdatePostRequestDto;
import com.socialmedia.mssspost.entity.Post;
import com.socialmedia.mssspost.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
                .username(request.getUsername())
                .createdAt(LocalDateTime.now())
                .build();
        return toResponse(postRepository.save(post));
    }

    @Override
    public PostResponseDto updatePost(Integer postId, UpdatePostRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

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
    public Page<PostResponseDto> searchPosts(String keyword, Pageable pageable) {
        Page<Post> posts = (keyword == null || keyword.isBlank())
                ? postRepository.findAll(pageable)
                : postRepository.findByContentContainingIgnoreCase(keyword, pageable);

        return posts.map(this::toResponse);
    }

    @Override
    public Page<PostResponseDto> getPostsByUsername(String username, Pageable pageable) {
        return postRepository.findByUsername(username, pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<PostResponseDto> getByFollowedUsernames(List<String> usernames, Pageable pageable) {
        return postRepository.findByUsernameIn(usernames, pageable)
                .map(this::toResponse);
    }

    @Override
    public List<PostResponseDto> getReportedPosts() {
        return postRepository.findByReported(true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public StatsResponseDto getStats() {
        StatsResponseDto statsDto = new StatsResponseDto();
        statsDto.setTotalPosts(postRepository.count());
        statsDto.setReportedPosts(postRepository.countByReportedTrue());

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        statsDto.setDailyPosts(postRepository.countByCreatedAtBetween(startOfDay, endOfDay));

        return statsDto;
    }

    private PostResponseDto toResponse(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .username(post.getUsername())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public void reportPost(Integer postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        postRepository.reportPost(postId);
    }

    @Transactional
    public void deleteAllByUsername(String username) {
        postRepository.deleteAllByUsername(username);
    }
}
