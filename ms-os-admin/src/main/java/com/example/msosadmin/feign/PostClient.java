package com.example.msosadmin.feign;

import com.example.msosadmin.dto.PostFeedItemDto;
import com.example.msosadmin.dto.StatsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ms-os-post")
public interface PostClient {
    @GetMapping("/posts/reported")
    List<PostFeedItemDto> getReportedPosts();

    @GetMapping("/posts/stats")
    StatsResponseDto getPostStats();
}

