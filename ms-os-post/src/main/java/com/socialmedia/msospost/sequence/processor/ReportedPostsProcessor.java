package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.PostDto;
import com.socialmedia.msospost.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportedPostsProcessor {
    private final PostClient postClient;

    public PostResponseDto getReported() {
        return postClient.getReportedPosts();
    }
}

