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
        PostResponseDto post = postClient.getPostById(context.getPostId());
        context.setPost(post);
    }
}
