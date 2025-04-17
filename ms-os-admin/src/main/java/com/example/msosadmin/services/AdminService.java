package com.example.msosadmin.services;

import com.example.msosadmin.dto.PostFeedItemDto;
import com.example.msosadmin.dto.StatsResponseDto;
import com.example.msosadmin.dto.UserProfileDTO;
import com.example.msosadmin.feign.PostClient;
import com.example.msosadmin.feign.UserClient;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public StatsResponseDto getDashboardStats() {
        return postClient.getPostStats();
    }

    public void deactivateUser(Integer userId) {
        userClient.deactivateUser(userId);
    }


}

