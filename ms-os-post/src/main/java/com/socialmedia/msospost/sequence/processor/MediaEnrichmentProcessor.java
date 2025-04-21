package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.CredentialClient;
import com.socialmedia.msospost.client.MediaClient;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.SequenceProcessor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MediaEnrichmentProcessor implements SequenceProcessor {

    private final MediaClient mediaClient;
    private final CredentialClient credentialClient;
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

    public void processAvatar(PostWorkflowContext context) {
        String username = context.getUsername();

        if (username == null || username.isBlank()) {
            System.out.println("⚠️ Username is null or blank; skipping avatar enrichment.");
            return;
        }

        try {
            String userId = credentialClient.getCredentialsByUsername(username).getUserId().toString();
            System.out.println("🧑 Avatar user ID: " + userId);

            mediaClient.findBySourceIdAndProvider(userId, "PROFILE")
                    .ifPresentOrElse(
                            media -> {
                                String imageUrl = "https://desmondzbucket.s3.ca-central-1.amazonaws.com/" + media.getS3Key();
                                if (context.getFinalDto() != null) {
                                    context.getFinalDto().setAvatarUrl(imageUrl);
                                    System.out.println("🧑‍🎨 Enriched PostFeedItemDto with avatar: " + imageUrl);
                                } else {
                                    System.out.println("⚠️ PostFeedItemDto is null; skipping avatar enrichment.");
                                }
                            },
                            () -> System.out.println("🚫 No avatar media found for username: " + username)
                    );
        } catch (Exception ex) {
            System.out.println("❌ Failed to fetch credentials for username: " + username);
            ex.printStackTrace();
        }
    }

}
