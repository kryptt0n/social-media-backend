package com.socialmedia.mssspost.service;

import com.socialmedia.mssspost.dto.CreatePostRequestDto;
import com.socialmedia.mssspost.dto.PostDto;
import com.socialmedia.mssspost.dto.StatsResponseDto;
import com.socialmedia.mssspost.dto.UpdatePostRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    PostDto createPost(CreatePostRequestDto request);
    PostDto updatePost(Integer postId, UpdatePostRequestDto request);
    void deletePost(Integer postId);
    PostDto getPostById(Integer id);
    Page<PostDto> searchPosts(String keyword, Pageable pageable);
    Page<PostDto> getPostsByUsername(String username, Pageable pageable);
    Page<PostDto> getByFollowedUsernames(List<String> usernames, Pageable pageable);
    List<PostDto> getAllPosts();
    List<PostDto> getReportedPosts();
    StatsResponseDto getStats();
    void reportPost(Integer postId);

    void deleteAllByUsername(String username);
}
