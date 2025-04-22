package com.example.msssfollow.repositories;


import com.example.msssfollow.entities.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    Optional<Follow> findByFollowerNameAndFollowedName(String followerName, String folowedName);
    List<Follow> findAllByFollowerName(String followerName);
    List<Follow> findAllByFollowedName(String followedName);
    void deleteAllByFollowedName(String followedName);
    void deleteAllByFollowerName(String followerName);
}
