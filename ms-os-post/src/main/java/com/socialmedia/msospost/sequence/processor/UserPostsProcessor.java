package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPostsProcessor {
    private final PostClient postClient;

    public Page<PostResponseDto> getPostsByUsername(String username, int page, int size) {
        return postClient.getPostsByUsername(username, page, size);
    }
}
