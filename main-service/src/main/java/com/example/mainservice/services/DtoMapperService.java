package com.example.mainservice.services;

import com.example.mainservice.dto.CommentDTO;
import com.example.mainservice.dto.PostDTO;
import com.example.mainservice.dto.UserDTO;
import com.example.mainservice.entities.Comment;
import com.example.mainservice.entities.Post;
import com.example.mainservice.entities.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DtoMapperService {

    public Function<Post, PostDTO> postToPostDTO(String username) {
        return post ->
                new PostDTO(
                        post.getId(),
                        post.getContent(),
                        post.getImageUrl(),
                        userToUserDTO(username).apply(post.getUser()),
                        post.getCreatedAt(),
                        post.getLikes().stream().anyMatch(like -> like.getUser().getUsername().equals(username)),
                        post.getLikes().size(),
                        post.getReported()
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
                user.getImageUrl(),
                user.getBio(),
                isFollowed(user, usernameCurrent),
                user.getFollows().size(),
                user.getFollowers().size(),
                user.isAccountNonLocked(),
                user.isPublic()
        );
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

    private boolean isFollowed(User user, String currentUsername) {
        return user.getFollows()
                .stream()
                .anyMatch(
                        follow -> follow
                                .getFollower()
                                .getUsername()
                                .equalsIgnoreCase(currentUsername));
    }
}
