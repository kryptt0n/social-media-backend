package com.vitalysukhinin.msosautomation.feign;

import com.vitalysukhinin.msosautomation.dtos.CreatePostRequestDto;
import com.vitalysukhinin.msosautomation.dtos.PostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-ss-post")
public interface PostClient {

    @PostMapping("/posts")
    PostDto createPost(@RequestBody CreatePostRequestDto request);

}
