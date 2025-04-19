package com.example.msosadmin.feign;

import com.example.msosadmin.dto.UserProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "ms-ss-user-profile-crud")
public interface UserClient {
    @GetMapping("/usercrud/users")
    List<UserProfileDTO> getAllUserProfiles();

    @PostMapping("/usercrud/deactivate/{userId}")
    void deactivateUser(@PathVariable("userId") Integer userId);

    @GetMapping("/usercrud/stats")
    UserStatsResponse getUserStats();

    record UserStatsResponse(Long totalUsers, Long publicAccounts, Long privateAccounts) {}
}

