package com.example.mainservice.services;

import com.example.mainservice.dto.DashboardStatsDTO;
import com.example.mainservice.dto.PostDTO;
import com.example.mainservice.dto.UserDTO;
import com.example.mainservice.entities.Post;
import com.example.mainservice.entities.User;
import com.example.mainservice.repositories.PostRepository;
import com.example.mainservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final DtoMapperService dtoMapperService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public AdminService(DtoMapperService dtoMapperService, PostRepository postRepository, UserRepository userRepository) {
        this.dtoMapperService = dtoMapperService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<PostDTO> getReportedPosts() {
        List<Post> posts = postRepository.findByReported(true);

        if (posts.isEmpty()) {
            return List.of();
        }
        return posts.stream()
                .map(post -> dtoMapperService.postToPostDTO(post.getUser().getUsername()).apply(post))
                .toList();
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        System.out.println("Users fetched from DB: " + users); // Debugging line

        if (users.isEmpty()) {
            System.out.println("empty");
            return List.of();
        }

        List<UserDTO> userDTOs = users.stream()
                .map(user -> dtoMapperService.userToUserDTO(user.getUsername()).apply(user))
                .collect(Collectors.toList());

        System.out.println("Mapped UserDTOs: " + userDTOs); // Debugging line

        return userDTOs;
    }


    public DashboardStatsDTO getDashboardStats() {
        // ✅ Fetch counts from the database
        long totalUsers = userRepository.count();
        long totalPosts = postRepository.count();
        long reportedPosts = postRepository.countByReportedTrue();

        // ✅ Fetch daily post count using Native Query
        List<Object[]> results = postRepository.getDailyPostCountNative();
        List<DashboardStatsDTO.DailyPostCount> dailyPosts = results.stream()
                .map(row -> new DashboardStatsDTO.DailyPostCount(row[0].toString(), ((Number) row[1]).longValue()))
                .collect(Collectors.toList());

        // ✅ Fetch public vs private account counts
        long publicAccounts = userRepository.countByIsPublicTrue();
        long privateAccounts = userRepository.countByIsPublicFalse();

        // ✅ Return aggregated dashboard stats
        return new DashboardStatsDTO(
                totalUsers,
                totalPosts,
                reportedPosts,
                dailyPosts,
                Map.of("public", publicAccounts, "private", privateAccounts)
        );
    }


}

