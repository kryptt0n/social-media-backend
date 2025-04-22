package com.example.msosuserprofile.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-ss-like", path = "/like")
public interface LikeClient {
    @DeleteMapping("/delete/{username}")
    void deleteLike(@PathVariable String username);
}
