package com.socialmedia.mssspost.service;

import com.socialmedia.mssspost.dto.CreatePostRequestDto;
import com.socialmedia.mssspost.dto.PostResponseDto;
import com.socialmedia.mssspost.dto.StatsResponseDto;
import com.socialmedia.mssspost.dto.UpdatePostRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(CreatePostRequestDto request);
    PostResponseDto updatePost(Integer postId, UpdatePostRequestDto request);
    void deletePost(Integer postId);
    PostResponseDto getPostById(Integer id);
    Page<PostResponseDto> searchPosts(String keyword, Pageable pageable);
    Page<PostResponseDto> getPostsByUsername(String username, Pageable pageable);
    Page<PostResponseDto> getByFollowedUsernames(List<String> usernames, Pageable pageable);
    List<PostResponseDto> getAllPosts();
    List<PostResponseDto> getReportedPosts();
    StatsResponseDto getStats();
    void reportPost(Integer postId);

    void deleteAllByUsername(String username);
}
