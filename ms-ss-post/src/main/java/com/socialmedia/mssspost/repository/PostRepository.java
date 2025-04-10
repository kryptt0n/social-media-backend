package com.socialmedia.mssspost.repository;

import com.socialmedia.mssspost.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {}
