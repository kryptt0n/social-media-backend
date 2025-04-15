package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.StatsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostStatsProcessor {
    private final PostClient postClient;

    public StatsResponseDto getStats() {
        return postClient.getPostStats();
    }
}
