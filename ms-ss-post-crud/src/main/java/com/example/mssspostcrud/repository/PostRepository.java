package com.example.mssspostcrud.repository;

import com.example.mssspostcrud.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    Page<Post> findAll(Pageable pageable);

    Page<Post> findByContentContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Post> findByUsername(String username, Pageable pageable);

    Page<Post> findByUsernameIn(List<String> usernames, Pageable pageable);

    List<Post> findByReported(Boolean reported);

    long countByReportedTrue();

    long countByCreatedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
