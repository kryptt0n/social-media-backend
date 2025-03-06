package com.j2ee.socialmedia.repositories;

import com.j2ee.socialmedia.dto.DashboardStatsDTO;
import com.j2ee.socialmedia.entities.Post;
import com.j2ee.socialmedia.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUserOrderByCreatedAtDesc(User user);
    List<Post> findAll(Sort sort);

    List<Post> findByReported(Boolean reported);

    long countByReportedTrue();

    @Query(value = "SELECT DATE(created_at) AS date, COUNT(*) " +
            "FROM post " +
            "WHERE created_at >= NOW() - INTERVAL 7 DAY " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY DATE(created_at)",
            nativeQuery = true)
    List<Object[]> getDailyPostCountNative();

}
