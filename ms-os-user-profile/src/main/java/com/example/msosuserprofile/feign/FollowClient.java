package com.example.msosuserprofile.feign;

import com.example.msosuserprofile.dto.FollowRequestDto;
import com.example.msosuserprofile.dto.FollowResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-ss-follow")
public interface FollowClient {
    @GetMapping("/follow/followers/{username}")
    List<String> getFollowers(@PathVariable String username);

    @GetMapping("/follow/followed/{username}")
    List<String> getFollowed(@PathVariable String username);

    @PostMapping("/follow/create")
    Void follow(FollowRequestDto followRequestDto);

    @DeleteMapping("/follow/unfollow")
    Void unfollow(@RequestParam String followerName, @RequestParam String followedName);

    @GetMapping("/follow/follow-data/{username}")
    FollowResponseDto getFollowData(@PathVariable String username);

    @GetMapping("/follow/is-followed")
    Boolean getIsFollowed(@RequestParam String currentName, @RequestParam String username);

    @DeleteMapping("/follow/delete/{username}")
    Void deleteFollow(@PathVariable String username);
}
