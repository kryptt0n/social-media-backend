package com.socialmedia.msospost.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-ss-follow")
public interface FollowClient {
    @GetMapping("/follow/followed/{username}")
    List<String> getFollowed(@PathVariable String username);
}
