package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.LikeClient;
import com.socialmedia.msospost.dto.LikeRequestDto;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.SequenceProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikePostProcessor implements SequenceProcessor {

    private final LikeClient likeClient;

    @Override
    public void process(PostWorkflowContext context) {
        LikeRequestDto request = new LikeRequestDto();
        request.setUsername(context.getUsername());
        request.setPostId(context.getPostId());

        System.out.println("👍 Sending like request: " + request);
        likeClient.likePost(request);
        System.out.println("✅ Post liked successfully");
    }
}
