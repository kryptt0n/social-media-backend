package com.example.mssspostcrud.service;

import com.example.mssspostcrud.dto.PostRequestDto;
import com.example.mssspostcrud.dto.StatsResponseDto;
import com.example.mssspostcrud.entity.Post;
import com.example.mssspostcrud.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post createPost(PostRequestDto postRequestDto) {
        Post post = new Post();
        post.setUsername(postRequestDto.getUsername());
        post.setContent(postRequestDto.getContent());
        return postRepository.save(post);
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return postRepository.findAll(pageable);
        }
        return postRepository.findByContentContainingIgnoreCase(keyword, pageable);
    }

    public Page<Post> getPostsByUsername(String username, Pageable pageable) {
        return postRepository.findByUsername(username, pageable);
    }

    public Page<Post> getFollowedPosts(List<String> usernameList, Pageable pageable) {
        return postRepository.findByUsernameIn(usernameList, pageable);
    }

    public List<Post> getReportedPosts() {
        return postRepository.findByReported(true);
    }

    public StatsResponseDto getStats() {
        StatsResponseDto statsDto = new StatsResponseDto();
        statsDto.setTotalPosts(postRepository.count());
        statsDto.setReportedPosts(postRepository.countByReportedTrue());

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        statsDto.setDailyPosts(postRepository.countByCreatedAtBetween(startOfDay, endOfDay));

        return statsDto;
    }
}
