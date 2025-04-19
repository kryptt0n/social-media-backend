package com.example.msssfollow.services;


import com.example.msssfollow.dto.FollowRequestDto;
import com.example.msssfollow.entities.Follow;
import com.example.msssfollow.repositories.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public Follow follow(FollowRequestDto requestDto) {
        Follow follow = new Follow();
        follow.setFollowerId(requestDto.getFollowerId());
        follow.setFollowedId(requestDto.getFollowedId());
        return followRepository.save(follow);
    }

    public void unfollow(Integer followerId, Integer followedId) {
        Optional<Follow> follow = followRepository.findByFollowedIdAndFollowerId(followerId, followedId);
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

    public boolean isFollowed(Integer userId, Integer currentUserId) {
        Optional<Follow> follow = followRepository.findByFollowedIdAndFollowerId(userId, currentUserId);
        return follow.isPresent();
    }

    public Integer countFollowers(Integer userId) {
        return followRepository.findAllByFollowedId(userId).size();
    }

    public Integer countFollowed(Integer userId) {
        return followRepository.findAllByFollowerId(userId).size();
    }
}

