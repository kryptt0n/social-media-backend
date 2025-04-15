package com.socialmedia.msospost.sequence;

import com.socialmedia.msospost.sequence.processor.CreatePostProcessor;
import com.socialmedia.msospost.sequence.processor.PostAggregatorProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostWorkflowRunner {

    private final CreatePostProcessor createPostProcessor;
    private final DummyPostFetchProcessor dummyPostFetchProcessor;
    private final PostAggregatorProcessor postAggregatorProcessor;

    public void runCreateFlow(PostWorkflowContext context) {
        System.out.println("▶️ Running Post Creation Workflow");
        createPostProcessor.process(context);
        dummyPostFetchProcessor.process(context);
        postAggregatorProcessor.process(context);
    }

    public void runFetchFlow(PostWorkflowContext context) {
        System.out.println("▶️ Running Post Fetch Workflow");
        dummyPostFetchProcessor.process(context);
        postAggregatorProcessor.process(context);
    }
}
