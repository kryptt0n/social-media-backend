package com.example.msosuserprofile.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-ss-comment", path = "/comment")
public interface CommentClient {
    @DeleteMapping("/delete/{username}")
    void deleteComment(@PathVariable("username") String username);
}
