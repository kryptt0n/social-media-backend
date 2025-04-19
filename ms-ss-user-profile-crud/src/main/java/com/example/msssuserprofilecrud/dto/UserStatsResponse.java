package com.example.msssuserprofilecrud.dto;

public record UserStatsResponse(
        long totalUsers,
        long publicAccounts,
        long privateAccounts
) {}
