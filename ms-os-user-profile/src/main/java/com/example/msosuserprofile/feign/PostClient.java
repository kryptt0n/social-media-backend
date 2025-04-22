package com.example.msosuserprofile.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-ss-post", path="/posts")
public interface PostClient {
    @DeleteMapping("/delete/{username}")
    void deletePost(@PathVariable String username);
}
