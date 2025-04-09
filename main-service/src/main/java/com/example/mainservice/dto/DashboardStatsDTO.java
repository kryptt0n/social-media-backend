package com.example.mainservice.dto;
import java.util.List;
import java.util.Map;

public class DashboardStatsDTO {
    private long totalUsers;
    private long totalPosts;
    private long reportedPosts;
    private List<DailyPostCount> dailyPosts;
    private Map<String, Long> accountTypes;

    public DashboardStatsDTO(long totalUsers, long totalPosts, long reportedPosts,
                             List<DailyPostCount> dailyPosts, Map<String, Long> accountTypes) {
        this.totalUsers = totalUsers;
        this.totalPosts = totalPosts;
        this.reportedPosts = reportedPosts;
        this.dailyPosts = dailyPosts;
        this.accountTypes = accountTypes;
    }

    public long getTotalUsers() { return totalUsers; }
    public long getTotalPosts() { return totalPosts; }
    public long getReportedPosts() { return reportedPosts; }
    public List<DailyPostCount> getDailyPosts() { return dailyPosts; }
    public Map<String, Long> getAccountTypes() { return accountTypes; }

    public static class DailyPostCount {
        private String date;
        private long count;

        public DailyPostCount(String date, long count) {
            this.date = date;
            this.count = count;
        }

        public String getDate() { return date; }
        public long getCount() { return count; }
    }
}
