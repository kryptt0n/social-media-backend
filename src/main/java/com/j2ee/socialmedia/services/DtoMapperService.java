package com.j2ee.socialmedia.services;

import com.j2ee.socialmedia.dto.CommentDTO;
import com.j2ee.socialmedia.dto.PostDTO;
import com.j2ee.socialmedia.dto.UserDTO;
import com.j2ee.socialmedia.entities.Comment;
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
     * @param usernameCurrent - current username
     * @return
     */
    public Function<User, UserDTO> userToUserDTO(String usernameCurrent) {
        return user -> new UserDTO(
                user.getUsername(),
                user.getProfilePicture(),
                user.getBio(),
                user.getFollowers()
                        .stream()
                        .anyMatch(follow -> follow.getFollower().getUsername().equals(usernameCurrent))
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

    public Function<Comment, CommentDTO> commentToCommentDTO() {
        return comment -> new CommentDTO(
                comment.getId(),
                comment.getContent(),
                postToPostDTO(comment.getUser().getUsername()).apply(comment.getPost()),
                userToUserDTO(comment.getUser().getUsername()).apply(comment.getUser()),
                comment.getCreatedAt()
        );
    }
}
