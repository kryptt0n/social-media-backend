package com.example.mssscomment.repository;

import com.example.mssscomment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPostIdOrderByCreatedAtDesc(Integer postId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.postId = :postId")
    void deleteAllByPostId(Integer postId);
}
