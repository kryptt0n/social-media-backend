package com.example.msosadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponseDto {
    private Long totalPosts;
    private Long reportedPosts;
    private Long dailyPosts;
}
