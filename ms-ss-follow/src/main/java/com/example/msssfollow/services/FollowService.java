package com.example.msssfollow.services;


import com.example.msssfollow.dto.FollowRequestDto;
import com.example.msssfollow.entities.Follow;
import com.example.msssfollow.repositories.FollowRepository;
import jakarta.transaction.Transactional;
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
        follow.setFollowerName(requestDto.getFollowerName());
        follow.setFollowedName(requestDto.getFollowedName());
        return followRepository.save(follow);
    }

    public void unfollow(String followerName, String followedName) {
        Optional<Follow> follow = followRepository.findByFollowerNameAndFollowedName(followerName, followedName);
        follow.ifPresent(followRepository::delete);
    }

    public List<String> getFollowers(String followedName) {
        return followRepository.findAllByFollowedName(followedName).stream()
                .map(Follow::getFollowerName)
                .toList();
    }

    public List<String> getFollowed(String followerName) {
        return followRepository.findAllByFollowerName(followerName).stream()
                .map(Follow::getFollowedName)
                .toList();
    }

    public boolean isFollowed(String currentName, String username) {
        Optional<Follow> follow = followRepository.findByFollowerNameAndFollowedName(currentName, username);
        return follow.isPresent();
    }

    public Integer countFollowers(String username) {
        return followRepository.findAllByFollowedName(username).size();
    }

    public Integer countFollowed(String username) {
        return followRepository.findAllByFollowerName(username).size();
    }

    @Transactional
    public void deleteAll(String username){
        followRepository.deleteAllByFollowerName(username);
        followRepository.deleteAllByFollowedName(username);
    }
}

