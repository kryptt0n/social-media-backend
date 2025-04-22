package com.example.mssscomment.service;

import com.example.mssscomment.dto.CommentRequestDto;
import com.example.mssscomment.dto.CommentResponseDto;
import com.example.mssscomment.entity.Comment;
import com.example.mssscomment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<CommentResponseDto> getCommentByPost(Integer postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());    }

    public CommentResponseDto createComment(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setContent(commentRequestDto.getContent());
        comment.setUsername(commentRequestDto.getUsername());
        comment.setPostId(commentRequestDto.getPostId());

        Comment saved = commentRepository.save(comment);

        return toResponse(saved);
    }

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    public void deleteAllByPostId(Integer postId) {
        try {
            System.out.println("🗑️ Deleting comments for postId=" + postId);
            commentRepository.deleteAllByPostId(postId);
        } catch (Exception e) {
            System.out.println("🔥 Exception while deleting comments: " + e.getMessage());
            throw e;
        }
    }

    private CommentResponseDto toResponse(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUsername(),
                comment.getPostId(),
                comment.getCreatedAt()
        );
    }

    @Transactional
    public void deleteAllByUsername(String username) {
        commentRepository.deleteAllByUsername(username);
    }
}
