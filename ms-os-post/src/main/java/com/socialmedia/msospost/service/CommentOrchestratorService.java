package com.socialmedia.msospost.service;

import com.socialmedia.msospost.client.CommentClient;
import com.socialmedia.msospost.dto.CommentRequestDto;
import com.socialmedia.msospost.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentOrchestratorService {

    private final CommentClient commentClient;

    public CommentResponseDto createComment(CommentRequestDto requestDto) {
        System.out.println("📝 Creating comment for postId=" + requestDto.getPostId());
        return commentClient.createComment(requestDto);
    }

}
