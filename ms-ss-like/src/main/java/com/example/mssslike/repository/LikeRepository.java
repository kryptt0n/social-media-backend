package com.example.mssslike.repository;

import com.example.mssslike.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUsernameAndPostId(String username, Integer postId);
    Integer countByPostId(Integer postId);
    void deleteAllByPostId(Integer postId);

    void deleteAllByUsername(String username);
}
