package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.LikeClient;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.SequenceProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeEnrichmentProcessor implements SequenceProcessor {

    private final LikeClient likeClient;

    @Override
    public void process(PostWorkflowContext context) {
        Integer likeCount = likeClient.getLikeCount(context.getPostId());
        System.out.println("❤️ Like count for post " + context.getPostId() + ": " + likeCount);
        context.setLikeCount(likeCount);
    }
}
