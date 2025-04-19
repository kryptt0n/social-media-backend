package com.example.msosadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponseDto {
    private Long totalPosts;
    private Long reportedPosts;
    private Long dailyPosts;
    private Long totalUsers;
    private Map<String, Long> accountTypes;
}