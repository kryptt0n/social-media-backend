package com.example.msosadmin.feign;

import com.example.msosadmin.dto.PostFeedItemDto;
import com.example.msosadmin.dto.StatsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-os-post")
public interface PostClient {
    @GetMapping("/posts/reported")
    List<PostFeedItemDto> getReportedPosts();

    @GetMapping("/posts/stats")
    StatsResponseDto getPostStats();

    record PostStatsResponse(Long totalPosts, Long reportedPosts, Long dailyPosts) {}

    @DeleteMapping("/posts/{postId}")
    ResponseEntity<Void> deletePost(@PathVariable("postId") Integer postId);

}

