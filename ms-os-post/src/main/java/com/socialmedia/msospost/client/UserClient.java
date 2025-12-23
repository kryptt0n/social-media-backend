package com.socialmedia.msospost.client;

import com.socialmedia.msospost.dto.UserShortDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-ss-user-profile-crud")
public interface UserClient {
    @GetMapping("/usernames/{username}")
    public UserShortDTO getUserByUsername(@PathVariable String username);
}
