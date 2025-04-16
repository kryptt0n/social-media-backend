package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.MediaClient;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.SequenceProcessor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MediaEnrichmentProcessor implements SequenceProcessor {

    private final MediaClient mediaClient;

    private static final String PROVIDER_POST = "POST"; // Avoids hardcoding throughout

    @Override
    public void process(PostWorkflowContext context) {
        Integer postId = context.getPostId();
        System.out.println("🖼️ Looking up media for postId: " + postId);

        mediaClient.findBySourceIdAndProvider(String.valueOf(postId), PROVIDER_POST)
            .ifPresentOrElse(
                media -> {
                    String imageUrl = "https://desmondzbucket.s3.ca-central-1.amazonaws.com/" + media.getS3Key();
                    if (context.getFinalDto() != null) {
                        context.getFinalDto().setImageUrl(imageUrl);
                        System.out.println("🖼️ Enriched PostFeedItemDto with imageUrl: " + imageUrl);
                    } else {
                        System.out.println("⚠️ PostFeedItemDto is null; skipping image enrichment.");
                    }
                },
                () -> System.out.println("🚫 No media found for postId: " + postId)
            );
    }
}
