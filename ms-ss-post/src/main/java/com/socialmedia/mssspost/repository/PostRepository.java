package com.socialmedia.mssspost.repository;

import com.socialmedia.mssspost.entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUsername(String username, Sort createdAt);
    List<Post> findAllByUsernameIn(List<String> usernames, Sort sort);
}
