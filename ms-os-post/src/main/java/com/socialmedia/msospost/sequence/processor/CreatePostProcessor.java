package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.CreatePostRequestDto;
import com.socialmedia.msospost.dto.PostResponseDto;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.SequenceProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatePostProcessor implements SequenceProcessor {

    private final PostClient postClient;

    @Override
    public void process(PostWorkflowContext context) {
        System.out.println("🚀 CreatePostProcessor called");

        CreatePostRequestDto request = new CreatePostRequestDto();
        request.setUsername(context.getUsername());
        request.setContent(context.getPost().getContent());
        request.setImageUrl(context.getPost().getImageUrl());

        PostResponseDto createdPost = postClient.createPost(request);

        System.out.println("✅ Post created with ID: " + createdPost.getId());

        context.setPost(createdPost);
        context.setPostId(createdPost.getId()); // Critical for downstream processors
    }
}
