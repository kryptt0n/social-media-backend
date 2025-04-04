package com.example.mainservice.repositories;

import com.example.mainservice.entities.Comment;
import com.example.mainservice.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
}
