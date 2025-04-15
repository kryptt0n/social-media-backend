package com.socialmedia.msospost.sequence;

import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DummyPostFetchProcessor implements SequenceProcessor {

    private final PostClient postClient;

    @Override
    public void process(PostWorkflowContext context) {
        System.out.println("🔍 DummyPostFetchProcessor called with postId: " + context.getPostId());

        if (context.getPostId() == null) {
            throw new IllegalStateException("postId is null — ensure CreatePostProcessor runs first");
        }

        PostResponseDto post = postClient.getPostById(context.getPostId());
        System.out.println("📦 Received post: " + post);
        context.setPost(post);

        System.out.println("📦 Fetched post with ID: " + post.getId());
    }
}
