package com.j2ee.socialmedia.repositories;

import com.j2ee.socialmedia.entities.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {
}
