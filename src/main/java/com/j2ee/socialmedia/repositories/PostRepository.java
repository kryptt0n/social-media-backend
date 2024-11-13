package com.j2ee.socialmedia.repositories;

import com.j2ee.socialmedia.entities.Post;
import com.j2ee.socialmedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUser(User user);
}
