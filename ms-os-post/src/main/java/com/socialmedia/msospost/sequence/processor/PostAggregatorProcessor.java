package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.dto.PostFeedItemDto;
import com.socialmedia.msospost.dto.PostResponseDto;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.SequenceProcessor;
import org.springframework.stereotype.Component;

@Component
public class PostAggregatorProcessor implements SequenceProcessor {

    @Override
    public void process(PostWorkflowContext context) {
        PostResponseDto post = context.getPost();
        if (post == null) {
            throw new IllegalStateException("Post must be fetched before aggregation");
        }

        PostFeedItemDto feedItemDto = PostFeedItemDto.builder()
                .postId(post.getId())
                .username(post.getUsername())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .likeCount(context.getLikeCount())
                .likedByCurrentUser(context.getLikedByCurrentUser())
                .comments(context.getComments())
                .avatarUrl(context.getUserProfile() != null ? context.getUserProfile().getAvatarUrl() : null)
                // ✅ imageUrl will be set later in MediaEnrichmentProcessor
                .build();

        context.setFinalDto(feedItemDto);

        System.out.println("🧩 Aggregated post into PostFeedItemDto: " + feedItemDto);
    }
}
