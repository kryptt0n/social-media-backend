package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.CommentClient;
import com.socialmedia.msospost.client.LikeClient;
import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.SequenceProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletePostProcessor implements SequenceProcessor {

    private final LikeClient likeClient;
    private final CommentClient commentClient;
    private final PostClient postClient;

    @Override
    public void process(PostWorkflowContext context) {
        Integer postId = context.getPostId();
        System.out.println("🗑️ Deleting all related data for postId: " + postId);

        // Cascade deletes
        likeClient.deleteAllByPostId(postId);
        commentClient.deleteAllByPostId(postId);

        // Delete the post itself
        postClient.deletePost(postId);

        System.out.println("✅ Post and related data deleted.");
    }
}
