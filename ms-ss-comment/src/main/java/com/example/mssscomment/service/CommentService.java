package com.example.mssscomment.service;

import com.example.mssscomment.dto.CommentRequestDto;
import com.example.mssscomment.dto.CommentResponseDto;
import com.example.mssscomment.entity.Comment;
import com.example.mssscomment.repository.CommentRepository;
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
        return toResponse(comment);
    }

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
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
}
