package com.example.msosadmin.services;

import com.example.msosadmin.dto.PostFeedItemDto;
import com.example.msosadmin.dto.StatsResponseDto;
import com.example.msosadmin.dto.UserProfileDTO;
import com.example.msosadmin.feign.PostClient;
import com.example.msosadmin.feign.UserClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final UserClient userClient;
    private final PostClient postClient;

    public AdminService(UserClient userClient, PostClient postClient) {
        this.userClient = userClient;
        this.postClient = postClient;

    }


    public List<UserProfileDTO> getAllUsers() {
        return userClient.getAllUserProfiles();
    }

    public List<PostFeedItemDto> getReportedPosts() {
        return postClient.getReportedPosts();
    }

    public StatsResponseDto getCombinedStats() {
        var postStats = postClient.getPostStats();
        var userStats = userClient.getUserStats();

        return new StatsResponseDto(
                postStats.getTotalPosts(),
                postStats.getReportedPosts(),
                postStats.getDailyPosts(),
                userStats.totalUsers(),
                Map.of(
                        "public", userStats.publicAccounts(),
                        "private", userStats.privateAccounts()
                )
        );
    }
    public void deactivateUser(Integer userId) {
        userClient.deactivateUser(userId);
    }


}

