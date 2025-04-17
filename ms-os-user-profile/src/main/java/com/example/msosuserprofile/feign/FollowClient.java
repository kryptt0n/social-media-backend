package com.example.msosuserprofile.feign;

import com.example.msosuserprofile.dto.FollowRequestDto;
import com.example.msosuserprofile.dto.FollowResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-ss-follow")
public interface FollowClient {
    @GetMapping("/follow/followers/{userId}")
    List<Integer> getFollowers(@PathVariable Integer userId);

    @GetMapping("/follow/followed/{userId}")
    List<Integer> getFollowed(@PathVariable Integer userId);

    @PostMapping("/follow")
    Void follow(FollowRequestDto followRequestDto);

    @DeleteMapping("/follow")
    Void unfollow(@RequestParam Integer followerId, @RequestParam Integer followedId);

    @GetMapping("/follow-data")
    FollowResponseDto getFollowData(@RequestParam Integer followerId, @RequestParam Integer followedId);
}
