package com.j2ee.socialmedia.services;

import com.j2ee.socialmedia.dto.PostDTO;
import com.j2ee.socialmedia.dto.UserDTO;
import com.j2ee.socialmedia.entities.Follow;
import com.j2ee.socialmedia.entities.Post;
import com.j2ee.socialmedia.entities.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DtoMapperService {

    public Function<Post, PostDTO> postToPostDTO(String username) {
        return post ->
                new PostDTO(
                        post.getId(),
                        post.getContent(),
                        post.getImage(),
                        userToUserDTO(username).apply(post.getUser()),
                        post.getCreatedAt(),
                        post.getLikes().stream().anyMatch(like -> like.getUser().getUsername().equals(username)),
                        post.getLikes().size()
                );
    }

    /**
     *
     * @param username - current username
     * @return
     */
    public Function<User, UserDTO> userToUserDTO(String username) {
        return user -> new UserDTO(
                user.getUsername(),
                user.getProfilePicture(),
                user.getBio(),
                user.getFollowers()
                        .stream()
                        .anyMatch(follow -> follow.getFollower().getUsername().equals(username))
        );
    }

    public Function<Follow, UserDTO> followerToUserDTO(User user) {
        return follow -> new UserDTO(
                follow.getFollower().getUsername(),
                follow.getFollower().getProfilePicture(),
                follow.getFollower().getBio(),
                user.getFollowers().contains(follow.getFollower()));
    }

    public Function<Follow, UserDTO> followedToUserDTO(User user) {
        return follow -> new UserDTO(
                follow.getFollowed().getUsername(),
                follow.getFollowed().getProfilePicture(),
                follow.getFollowed().getBio(),
                user.getFollows().contains(follow.getFollowed()));
    }
}
