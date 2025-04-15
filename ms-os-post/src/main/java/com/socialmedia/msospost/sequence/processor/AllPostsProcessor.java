package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AllPostsProcessor {
    private final PostClient postClient;

    public List<PostResponseDto> getAll() {
        return postClient.getAllPosts();
    }
}
