package com.socialmedia.msospost.sequence;

import com.socialmedia.msospost.sequence.processor.CommentEnrichmentProcessor;
import com.socialmedia.msospost.sequence.processor.CreatePostProcessor;
import com.socialmedia.msospost.sequence.processor.PostAggregatorProcessor;
import com.socialmedia.msospost.sequence.processor.LikeEnrichmentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostWorkflowRunner {

    private final CreatePostProcessor createPostProcessor;
    private final DummyPostFetchProcessor dummyPostFetchProcessor;
    private final PostAggregatorProcessor postAggregatorProcessor;
    private final LikeEnrichmentProcessor likeEnrichmentProcessor;
    private final CommentEnrichmentProcessor commentEnrichmentProcessor;

    public void runCreateFlow(PostWorkflowContext context) {
        System.out.println("▶️ Running Post Creation Workflow");
        createPostProcessor.process(context);
        dummyPostFetchProcessor.process(context);
        likeEnrichmentProcessor.process(context);
        commentEnrichmentProcessor.process(context);
        postAggregatorProcessor.process(context);
    }

    public void runFetchFlow(PostWorkflowContext context) {
        System.out.println("▶️ Running Post Fetch Workflow");
        dummyPostFetchProcessor.process(context);
        likeEnrichmentProcessor.process(context);
        commentEnrichmentProcessor.process(context);
        postAggregatorProcessor.process(context);
    }
}
