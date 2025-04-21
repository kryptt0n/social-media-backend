package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.PostResponseDto;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.SequenceProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FetchPostByIdProcessor implements SequenceProcessor {

    private final PostClient postClient;

    @Override
    public void process(PostWorkflowContext context) {
        if (context.getPostId() == null) {
            throw new IllegalStateException("Post ID must not be null before fetching post");
        }

        System.out.println("🔍 FetchPostByIdProcessor called with postId: " + context.getPostId());
        PostResponseDto post = postClient.getPostById(context.getPostId());

        context.setPost(post);
        context.setUsername(post.getUsername());

        System.out.println("📦 Fetched post with ID: " + post.getId() + ", username: " + post.getUsername());
    }
}
