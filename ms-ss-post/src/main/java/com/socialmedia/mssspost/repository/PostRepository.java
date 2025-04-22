package com.socialmedia.mssspost.repository;

import com.socialmedia.mssspost.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUsername(String username, Sort createdAt);
    List<Post> findAllByUsernameIn(List<String> usernames, Sort sort);
    Page<Post> findByContentContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Post> findByUsername(String username, Pageable pageable);
    Page<Post> findByUsernameIn(List<String> usernames, Pageable pageable);
    List<Post> findByReported(boolean reported);
    long countByReportedTrue();
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.reported = true WHERE p.id = :postId")
    void reportPost(Integer postId);

    void deleteAllByUsername(String username);
}
