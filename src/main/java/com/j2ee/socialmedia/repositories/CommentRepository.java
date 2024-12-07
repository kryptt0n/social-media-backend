package com.j2ee.socialmedia.repositories;

import com.j2ee.socialmedia.entities.Comment;
import com.j2ee.socialmedia.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
}
