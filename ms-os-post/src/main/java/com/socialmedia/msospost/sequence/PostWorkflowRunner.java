package com.socialmedia.msospost.sequence;

import com.socialmedia.msospost.sequence.processor.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostWorkflowRunner {

    private final CreatePostProcessor createPostProcessor;
    private final FetchPostByIdProcessor fetchPostByIdProcessor;
    private final PostAggregatorProcessor postAggregatorProcessor;
    private final LikeEnrichmentProcessor likeEnrichmentProcessor;
    private final CommentEnrichmentProcessor commentEnrichmentProcessor;
    private final MediaEnrichmentProcessor mediaEnrichmentProcessor;

    public void runCreateFlow(PostWorkflowContext context) {
        System.out.println("▶️ Running Post Creation Workflow");
        createPostProcessor.process(context);
        fetchPostByIdProcessor.process(context);
        likeEnrichmentProcessor.process(context);
        commentEnrichmentProcessor.process(context);
        postAggregatorProcessor.process(context);
    }

    public void runFetchFlow(PostWorkflowContext context) {
        System.out.println("▶️ Running Post Fetch Workflow");
        fetchPostByIdProcessor.process(context);
        likeEnrichmentProcessor.process(context);
        commentEnrichmentProcessor.process(context);
        postAggregatorProcessor.process(context);
        mediaEnrichmentProcessor.process(context);
        mediaEnrichmentProcessor.processAvatar(context);
    }
}
