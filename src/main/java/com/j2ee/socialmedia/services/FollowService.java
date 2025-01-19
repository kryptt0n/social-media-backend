package com.j2ee.socialmedia.services;

import com.j2ee.socialmedia.dto.UserDTO;
import com.j2ee.socialmedia.entities.Follow;
import com.j2ee.socialmedia.entities.User;
import com.j2ee.socialmedia.repositories.FollowRepository;
import com.j2ee.socialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final DtoMapperService dtoMapperService;
    private final UserRepository userRepository;

    @Autowired
    public FollowService(UserRepository userRepository, FollowRepository followRepository, DtoMapperService dtoMapperService) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.dtoMapperService = dtoMapperService;
    }

    /**
     * Add new follower
     * @param followUsername - username of the user that is going to be followed
     * @param username - username of user that is going to be a follower
     */
    public void follow(String followUsername, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<User> userToFollow = userRepository.findByUsername(followUsername);
        if (userToFollow.isPresent() && user.isPresent()) {
            Follow follow = new Follow();
            follow.setFollower(user.get());
            follow.setFollowed(userToFollow.get());
            follow.setFollowedAt(LocalDateTime.now());
            followRepository.save(follow);
        }
    }

    @Transactional
    public void unfollow(String followedUsername, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<User> userToUnfollow = userRepository.findByUsername(followedUsername);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + username);
        }

        if (userToUnfollow.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User to unfollow not found: " + followedUsername);
        }

        Optional<Follow> follow = followRepository.findByFollowedAndFollower(userToUnfollow.get(), user.get());
        if (follow.isPresent()) {
            followRepository.delete(follow.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Follow relationship not found");
        }
    }

    public List<UserDTO> getFollowers(String usernameFollower, String currentUsername) {
        Optional<User> userOptional = userRepository.findByUsername(usernameFollower);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Follow> follows = followRepository.findAllByFollowed(user);
            List<UserDTO> users = follows
                    .stream()
                    .map(Follow::getFollower)
                    .map(dtoMapperService.userToUserDTO(currentUsername))
                    .toList();

            return users;
        }
        return List.of();
    }

    public List<UserDTO> getFollowed(String usernameFollowed, String currentUsername) {
        Optional<User> userOptional = userRepository.findByUsername(usernameFollowed);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Follow> followed = followRepository.findAllByFollower(user);
            List<UserDTO> users = followed
                    .stream()
                    .map(Follow::getFollowed)
                    .map(dtoMapperService.userToUserDTO(currentUsername))
                    .toList();

            return users;
        }
        return List.of();
    }
}
