package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchPostsProcessor {
    private final PostClient postClient;

    public Page<PostResponseDto> searchPosts(String keyword, int page, int size) {
        return postClient.searchPosts(keyword, page, size);
    }
}
