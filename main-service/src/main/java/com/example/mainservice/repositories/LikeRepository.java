package com.example.mainservice.repositories;

import com.example.mainservice.entities.Like;
import com.example.mainservice.entities.Post;
import com.example.mainservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserAndPost(User user, Post post);
}
