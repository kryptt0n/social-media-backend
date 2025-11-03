package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.CommentClient;
import com.socialmedia.msospost.dto.CommentResponseDto;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.SequenceProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentEnrichmentProcessor implements SequenceProcessor {

    private final CommentClient commentClient;

    @Override
    public void process(PostWorkflowContext context) {
        Integer postId = context.getPostId();
//        System.out.println("💬 Fetching comments for postId: " + postId);

        List<CommentResponseDto> comments = commentClient.getCommentsByPost(postId);
//        System.out.println("💬 Fetched comments: " + comments); // ← 🔍 ADD THIS HERE

        context.setComments(comments);

//        System.out.println("💬 Received " + comments.size() + " comments");
    }
}
