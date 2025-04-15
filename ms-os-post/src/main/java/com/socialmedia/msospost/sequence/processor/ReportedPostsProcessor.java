package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportedPostsProcessor {
    private final PostClient postClient;

    public List<PostResponseDto> getReported() {
        return postClient.getReportedPosts();
    }
}

