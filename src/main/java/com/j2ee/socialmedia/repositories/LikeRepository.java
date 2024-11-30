package com.j2ee.socialmedia.repositories;

import com.j2ee.socialmedia.entities.Like;
import com.j2ee.socialmedia.entities.Post;
import com.j2ee.socialmedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserAndPost(User user, Post post);
}
