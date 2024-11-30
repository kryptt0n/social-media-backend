package com.j2ee.socialmedia.repositories;

import com.j2ee.socialmedia.entities.Follow;
import com.j2ee.socialmedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {
    Optional<Follow> findByFollowedAndFollower(User followed, User follower);
    List<Follow> findAllByFollower(User follower);
    List<Follow> findAllByFollowed(User followed);
}
