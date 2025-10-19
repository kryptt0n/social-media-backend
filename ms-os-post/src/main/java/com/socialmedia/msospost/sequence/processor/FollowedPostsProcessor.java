package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.FollowClient;
import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.PostDto;

import com.socialmedia.msospost.dto.PostResponseDto;
import org.springframework.data.domain.Page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowedPostsProcessor {

    private final FollowClient followClient;
    private final PostClient postClient;

    public PostResponseDto getFollowedPosts(String username, int page, int size) {
        List<String> followedUsernames = followClient.getFollowed(username);
        if (followedUsernames.isEmpty())
            return PostResponseDto.builder().posts(new ArrayList<>()).cursor(LocalDateTime.now()).hasMore(false).build();

        return postClient.getFollowedPosts(followedUsernames, page, size);
    }
}
