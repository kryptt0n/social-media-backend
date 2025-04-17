package com.example.msssfollow.repositories;


import com.example.msssfollow.entities.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    Optional<Follow> findByFollowedIdAndFollowerId(Integer followedId, Integer followerId);
    List<Follow> findAllByFollowerId(Integer followerId);
    List<Follow> findAllByFollowedId(Integer followedId);
}
