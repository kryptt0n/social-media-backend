package com.example.msssfollow.services;


import com.example.msssfollow.entities.Follow;
import com.example.msssfollow.repositories.FollowRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public void follow(Integer followerId, Integer followedId) {
        followRepository.save(new Follow(followerId, followedId));
    }

    public void unfollow(Integer followerId, Integer followedId) {
        Optional<Follow> follow = followRepository.findByFollowedIdAndFollowerId(followedId, followerId);
        follow.ifPresent(followRepository::delete);
    }

    public List<Integer> getFollowers(Integer userId) {
        return followRepository.findAllByFollowedId(userId).stream()
                .map(Follow::getFollowerId)
                .toList();
    }

    public List<Integer> getFollowed(Integer userId) {
        return followRepository.findAllByFollowerId(userId).stream()
                .map(Follow::getFollowedId)
                .toList();
    }
}

