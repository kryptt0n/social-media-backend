package com.socialmedia.mssspost.service;

import com.socialmedia.mssspost.dto.CreatePostRequestDto;
import com.socialmedia.mssspost.dto.PostResponseDto;
import com.socialmedia.mssspost.dto.UpdatePostRequestDto;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(CreatePostRequestDto request);
    PostResponseDto updatePost(Integer postId, UpdatePostRequestDto request);
    void deletePost(Integer postId);
    PostResponseDto getPostById(Integer id);
    List<PostResponseDto> getAllPosts();
    List<PostResponseDto> getPostsByUsername(String username);
    List<PostResponseDto> getByFollowedUsernames(List<String> usernames);
}
